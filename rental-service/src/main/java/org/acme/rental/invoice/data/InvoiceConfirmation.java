package org.acme.rental.invoice.data;

import java.time.LocalDate;

public class InvoiceConfirmation {
  public Invoice invoice;
  public boolean paid;

  public class Invoice {
    public double totalPrice;
    public boolean paid;
    public InvoiceReservation reservation;

    public Invoice() {
    };

    public Invoice(double totalPrice, boolean paid, InvoiceReservation reservation) {
      this.totalPrice = totalPrice;
      this.paid = paid;
      this.reservation = reservation;
    }

  }

  public class InvoiceReservation {
    public long id;
    public String userId;
    public LocalDate startDay;
  }

}
