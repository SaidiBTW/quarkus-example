package org.acme.inventory.service;

import java.util.List;
import java.util.Optional;

import org.acme.inventory.model.Car;
import org.acme.inventory.repository.CarRepository;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Query;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@GraphQLApi
public class GraphQLInventoryService {
  @Inject
  CarRepository carRepository;

  @Query
  public List<Car> cars() {
    return carRepository.listAll();
  }

  @Transactional
  @Mutation
  public Car register(Car car) {
    carRepository.persist(car);
    Log.info("Persisting " + car);
    return car;
  }

  @Transactional
  @Mutation
  public boolean remove(String licensePlateNumber) {
    Optional<Car> toBeRemoved = carRepository.findByLicensePlateNumberOptional(licensePlateNumber);
    // List<Car> cars = inventory.getCars();
    // Optional<Car> toBeRemoved = cars.stream()
    // .filter(car -> car.licensePlateNumber.equals(licensePlateNumber))
    // .findFirst();

    if (toBeRemoved.isPresent()) {
      carRepository.delete(toBeRemoved.get());
      return true;
    } else {
      return false;
    }
  }

}
