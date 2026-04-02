package org.acme.billing;

import java.util.Random;

import org.acme.billing.data.InvoiceConfirmation;
import org.acme.billing.model.Invoice;
import org.acme.billing.model.InvoiceAdjust;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PaymentRequester {
  private final Random random = new Random();

  @Incoming("invoices-requests")
  @Outgoing("invoices-confirmations")
  @Blocking
  public InvoiceConfirmation requestPayment(Invoice invoice) {
    payment(invoice.reservation.userId, invoice.totalPrice, invoice);

    invoice.paid = true;
    invoice.update();
    Log.infof("Invoice of %s is paid", invoice);

    return new InvoiceConfirmation(invoice, true);

  }

  private void payment(String userId, double price, Object data) {
    Log.infof("Request for payment for user: %s, price: %d, data: %s", userId, price, data);
    try {
      Thread.sleep(random.nextInt(1000, 5000));
    } catch (InterruptedException e) {
      Log.error("Sleep interrupted, ", e);
    }
  }

  @Incoming("invoices-adjust")
  @Blocking
  @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
  public void requestAdjustment(InvoiceAdjust invoiceAdjust) {
    Log.info("Received invoice adjustmnet: " + invoiceAdjust);
    payment(invoiceAdjust.userId, invoiceAdjust.price, invoiceAdjust);
    invoiceAdjust.persist();
    Log.infof("Invoice adjustment of %s is paid.", invoiceAdjust);

  }

  // @Incoming("invoice-confirmations")
  // public void consume(InvoiceConfirmation invoiceConfirmation){
  // System.out.println(invoiceConfirmation);
  // }

}
