package org.acme.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Car {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;
  public String licensePlateNumber;
  public String manufacturer;
  public String model;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLicensePlateNumber() {
    return licensePlateNumber;
  }

  public void setLicensePlateNumber(String licensePlateNumber) {
    this.licensePlateNumber = licensePlateNumber;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  // public Car(Long id, String licensePlateNumber, String manufacturer, String
  // model) {
  // this.id = id;
  // this.licensePlateNumber = licensePlateNumber;
  // this.manufacturer = manufacturer;
  // this.model = model;
  // }

  // public Long getId() {
  // return id;
  // }

  // public String getLicensePlateNumber() {
  // return licensePlateNumber;
  // }

  // public String getManufacturer() {
  // return manufacturer;
  // }

  // public String getModel() {
  // return model;
  // }

}