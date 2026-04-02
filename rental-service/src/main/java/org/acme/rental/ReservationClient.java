package org.acme.rental;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.acme.rental.reservation.Car;
import org.acme.rental.reservation.Reservation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestQuery;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@RegisterRestClient(baseUri = "http://localhost:8081")
@Path("/reservation")
public interface ReservationClient {
  @GET
  @Path("availability")
  public Collection<Car> availableReservation(@RestQuery LocalDate startDate, @RestQuery LocalDate endDate);

  @POST
  public Reservation make(Reservation reservation);

  @GET
  @Path("all")
  public List<Reservation> allReservations();

  @GET
  @Path("{id}")
  public Reservation getById(Long id);

}
