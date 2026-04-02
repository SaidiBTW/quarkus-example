package org.acme.billing.model;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "Invoice")
public class Invoice extends PanacheMongoEntity {
  public double totalPrice;
  public boolean paid;
  public Reservation reservation;

  public Invoice() {
  };

  public Invoice(double totalPrice, boolean paid, Reservation reservation) {
    this.totalPrice = totalPrice;
    this.paid = paid;
    this.reservation = reservation;
  }
}
