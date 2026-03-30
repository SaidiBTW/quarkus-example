package org.acme.reservation.inventory;

import java.util.List;

import io.smallrye.mutiny.Uni;

public interface InventoryClient {
  Uni<List<Car>> allCars();
}
