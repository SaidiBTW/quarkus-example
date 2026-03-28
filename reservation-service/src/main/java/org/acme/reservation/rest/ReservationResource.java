package org.acme.reservation.rest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.acme.reservation.inventory.Car;
import org.acme.reservation.inventory.GraphQLInventoryClient;
import org.acme.reservation.rental.Rental;
import org.acme.reservation.rental.RentalClient;
import org.acme.reservation.reservation.InMemoryReservationsRepository;
import org.acme.reservation.reservation.Reservation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.logging.Log;
import io.smallrye.graphql.client.GraphQLClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
public class ReservationResource {
  private final GraphQLInventoryClient inventoryClient;
  private final InMemoryReservationsRepository reservationsRepo;
  private final RentalClient rentalClient;

  @Inject
  jakarta.ws.rs.core.SecurityContext context;

  ReservationResource(
      InMemoryReservationsRepository reservationsRepository,
      @GraphQLClient("inventory") GraphQLInventoryClient inventoryClient,
      @RestClient RentalClient rentalClient) {
    this.inventoryClient = inventoryClient;
    this.reservationsRepo = reservationsRepository;
    this.rentalClient = rentalClient;
  }

  @GET
  @Path("availability")
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Car> availableReservations(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate) {
    // Get all cars in inventory
    List<Car> availableCars = inventoryClient.allCars();
    // Create a map from id to car
    Map<Long, Car> carsById = new HashMap<>();
    for (Car car : availableCars) {
      carsById.put(car.id, car);
    }

    // Get all current reservations
    List<Reservation> reservations = reservationsRepo.findAll();
    // For each reservation remove car from the map
    for (Reservation reservation : reservations) {
      if (reservation.isReserved(startDate, endDate)) {
        carsById.remove(reservation.carId);
      }
    }

    return carsById.values();

  }

  @Consumes(MediaType.APPLICATION_JSON)
  @POST
  public Reservation make(Reservation reservation) {
    Reservation result = reservationsRepo.save(reservation);
    reservation.userId = context.getUserPrincipal() != null ? context.getUserPrincipal().getName() : "anonymous";
    if (reservation.startDay.equals(LocalDate.now())) {
      Rental rental = rentalClient.start(result.userId, result.id);
      Log.info("Successfully started rental " + rental);
    }
    return result;
  }

  @GET
  @Path("all")
  public Collection<Reservation> allReservations() {
    String userId = context.getUserPrincipal() != null ? context.getUserPrincipal().getName() : null;
    return reservationsRepo.findAll().stream()
        .filter(reservation -> userId == null || userId.equals(reservation.userId))
        .collect(Collectors.toList());
  }

}
