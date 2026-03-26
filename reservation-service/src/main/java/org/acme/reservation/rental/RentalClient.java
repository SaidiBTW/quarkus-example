package org.acme.reservation.rental;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestPath;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@RegisterRestClient(baseUri = "http://localhost:8082")
@Path("/rental")
public interface RentalClient {
  @POST
  @Path("/start/{userId}/{reservationId}")
  public Rental start(@RestPath String userId, @RestPath Long reservationId);
}
