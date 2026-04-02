package org.acme.rental.invoice;

import org.acme.rental.Rental;
import org.acme.rental.invoice.data.InvoiceConfirmation;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InvoiceConfirmationService {
  @Incoming("invoices-confirmations")
  public void invoicePaid(InvoiceConfirmation invoiceConfirmation) {
    Log.info("Received invoice confirmation " + invoiceConfirmation);

    if (!invoiceConfirmation.paid) {
      Log.warn("Received unpaid invoice confirmation - " + invoiceConfirmation);
    }

    InvoiceConfirmation.InvoiceReservation reservation = invoiceConfirmation.invoice.reservation;

    Rental.findByUserAnReservationIdsOptional(reservation.userId, reservation.id)
        .ifPresentOrElse(rental -> {
          rental.paid = true;
          rental.update();
        }, () -> {
          Rental rental = new Rental();
          rental.userId = reservation.userId;
          rental.reservationId = reservation.id;
          rental.startDate = reservation.startDay;
          rental.active = false;
          rental.paid = true;
          rental.persist();
        });
  }

}
