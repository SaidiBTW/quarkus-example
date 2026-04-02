package org.acme.billing.model;

import java.time.LocalDate;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

public class InvoiceAdjust extends PanacheMongoEntity {
  public String rentalId;
  public String userId;
  public LocalDate actualEndDate;
  public double price;
  public boolean paid;

  public InvoiceAdjust(String rentalId, String userId, LocalDate actualEndDate, double price, boolean paid) {
    this.rentalId = rentalId;
    this.userId = userId;
    this.actualEndDate = actualEndDate;
    this.price = price;
    this.paid = paid;
  }

  @Override
  public String toString() {
    return "InvoiceAdjust [rentalId=" + rentalId + ", userId=" + userId + ", actualEndDate=" + actualEndDate
        + ", price=" + price + ", paid=" + paid + ", id=" + id + "]";
  }

}
