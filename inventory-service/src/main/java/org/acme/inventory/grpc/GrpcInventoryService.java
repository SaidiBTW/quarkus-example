package org.acme.inventory.grpc;

import java.util.Optional;

import org.acme.inventory.model.Car;
import org.acme.inventory.model.CarResponse;
import org.acme.inventory.model.InsertCarRequest;
import org.acme.inventory.model.InventoryService;
import org.acme.inventory.model.RemoveCarRequest;
import org.acme.inventory.repository.CarRepository;

import io.quarkus.grpc.GrpcService;
import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@GrpcService
public class GrpcInventoryService implements InventoryService {

  @Inject
  CarRepository carRepository;

  @Override
  @Blocking
  public Multi<CarResponse> add(Multi<InsertCarRequest> requests) {
    return requests.map(
        request -> {
          Car car = new Car();
          car.setLicensePlateNumber(request.getLicensePlateNumber());
          car.setManufacturer(request.getManufacturer());
          car.setModel(request.getModel());
          return car;

        }).onItem().invoke(car -> {
          QuarkusTransaction.requiringNew().run(() -> {
            carRepository.persist(car);
            Log.info("Persisting " + car);
          });
        }

    ).map(car -> CarResponse.newBuilder().setLicensePlateNumber(car.getLicensePlateNumber())
        .setManufacturer(car.getManufacturer()).setModel(car.getModel()).setId(car.getId()).build());

  }

  @Override
  @Blocking
  @Transactional
  public Uni<CarResponse> remove(RemoveCarRequest request) {
    Optional<Car> optionCar = carRepository.findByLicensePlateNumberOptional(request.getLicensePlateNumber());

    if (optionCar.isPresent()) {
      Car removedCar = optionCar.get();
      carRepository.delete(removedCar);
      return Uni.createFrom().item(CarResponse.newBuilder().setLicensePlateNumber(removedCar.getLicensePlateNumber())
          .setManufacturer(removedCar.getManufacturer()).setId(removedCar.getId()).build());
    }
    return Uni.createFrom().nullItem();
  }

  // @Override
  // public Uni<CarResponse> add(InsertCarRequest request) {
  // Car car = new Car();
  // car.licensePlateNumber = request.getLicensePlateNumber();
  // car.manufacturer = request.getManufacturer();
  // car.model = request.getModel();
  // car.id = CarInventory.ids.incrementAndGet();
  // Log.info("Persisting " + car);
  // inventory.getCars().add(car);

  // return Uni.createFrom().item(CarResponse.newBuilder()
  // .setLicensePlateNumber(car.licensePlateNumber)
  // .setManufacturer(car.manufacturer)
  // .setModel(car.model)
  // .setId(car.id).build());

  // }

  // @Override
  // @Blocking
  // public Multi<CarResponse> remove(Multi<RemoveCarRequest> requests) {
  // return requests.map(
  // request -> {
  // Car car = new Car();
  // car.setLicensePlateNumber(request.getLicensePlateNumber());
  // car.setManufacturer(request.get);
  // car.setModel(request.getModel());
  // }
  // )
  // Optional<Car> optionalCar = inventory.getCars().stream()
  // .filter(car ->
  // request.getLicensePlateNumber().equals(car.licensePlateNumber))
  // .findFirst();

  // if (optionalCar.isPresent()) {
  // Car removedCar = optionalCar.get();
  // inventory.getCars().remove(removedCar);
  // return
  // Uni.createFrom().item(CarResponse.newBuilder().setLicensePlateNumber(removedCar.licensePlateNumber)
  // .setManufacturer(removedCar.manufacturer).setModel(removedCar.model).setId(removedCar.id).build());
  // }

  // return Uni.createFrom().nullItem();

  // }

  // @Override
  // public Uni<CarResponse> add(InsertCarRequest request) {

  // Car car = new Car();
  // car.licensePlateNumber = request.getLicensePlateNumber();
  // car.manufacturer = request.getManufacturer();
  // car.model = request.getModel();
  // car.id = CarInventory.ids.incrementAndGet();

  // Log.info("Persisting " + car);
  // inventory.getCars().add(car);
  // return Uni.createFrom()
  // .item(CarResponse.newBuilder().setLicensePlateNumber(car.licensePlateNumber).setModel(car.model)
  // .setManufacturer(car.manufacturer).setId(car.id).build());
  // }

  // @Override
  // public Uni<CarResponse> remove(RemoveCarRequest request) {
  // // TODO Auto-generated method stub
  // throw new UnsupportedOperationException("Unimplemented method 'remove'");
  // }

}
