package org.acme.billing;

import org.acme.billing.data.ReservationInvoice;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.Reservation;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InvoiceProcessor {

  @Incoming("invoices")
  @Outgoing("invoices-requests")
  public Message<Invoice> processInvoice(Message<JsonObject> message) {
    ReservationInvoice invoiceMessage = message.getPayload().mapTo(ReservationInvoice.class);

    Reservation reservation = invoiceMessage.reservation;

    Invoice invoice = new Invoice(
        invoiceMessage.price,
        false,
        reservation);
    invoice.persist();

    Log.infof("Processing Invoice: %s", invoice.toString());

    return message.withPayload(invoice);

  }

}
