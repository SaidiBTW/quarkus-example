package org.acme.reservation.rest;

import org.acme.reservation.reservation.Reservation;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "/admin/reservation")
public interface ReservationCrudResource extends PanacheEntityResource<Reservation, Long> {

}
