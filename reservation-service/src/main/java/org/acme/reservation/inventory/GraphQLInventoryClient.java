package org.acme.reservation.inventory;

import java.util.List;

import org.eclipse.microprofile.graphql.Query;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;

@GraphQLClientApi(configKey = "inventory")
public interface GraphQLInventoryClient extends InventoryClient {
  @Query("cars")
  List<Car> allCars();
}