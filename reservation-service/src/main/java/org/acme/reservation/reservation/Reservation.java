package org.acme.reservation.reservation;

import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Reservation extends PanacheEntity {

  public Long carId;
  public String userId;
  public LocalDate startDay;
  public LocalDate endDay;

  /**
   * Check if the given duration oeverlaps with this reservation
   * 
   * @return true if the dates overlap with teh reservation, false
   *         otherwise
   */
  public boolean isReserved(LocalDate startDay, LocalDate endDay) {
    return (!(this.endDay.isBefore(startDay) || this.startDay.isAfter(endDay)));
  }

  @Override
  public String toString() {
    return "Reservation{" +
        ",carId=" + carId +
        ",userId=" + userId +
        ",startDay=" + startDay +
        ",endDay=" + endDay +
        "}";
  }

}
