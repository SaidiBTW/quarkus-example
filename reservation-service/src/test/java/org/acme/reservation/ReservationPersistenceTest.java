package org.acme.reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.acme.reservation.reservation.Reservation;
import org.acme.reservation.reservation.ReservationsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class ReservationPersistenceTest {

  @Inject
  ReservationsRepository repository;

  @Test
  @Transactional
  public void testCreateReservation() {
    Reservation reservation = new Reservation();
    reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS);
    reservation.endDay = LocalDate.now().plus(12, ChronoUnit.DAYS);
    reservation.carId = 384L;
    reservation.persist();

    Assertions.assertNotNull(reservation.id);
    Assertions.assertEquals(1, Reservation.count());
    Reservation persistedReservation = Reservation.findById(reservation.id);
    Assertions.assertNotNull(persistedReservation);
    Assertions.assertEquals(reservation.carId, persistedReservation.carId);
  }

}
