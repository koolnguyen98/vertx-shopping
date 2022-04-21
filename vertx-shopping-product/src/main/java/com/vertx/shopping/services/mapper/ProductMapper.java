package com.vertx.shopping.services.mapper;

import com.vertx.shopping.services.Product;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.UUID;

public class ProductMapper {
  public static Product from(Row row) {
    UUID id = UUID.fromString(row.getString("id"));
    String productName = row.getString("product_name");
    double price = row.getDouble("price");
    return new Product(id, productName, price);
  }

  public static Tuple to(Product product) {
    return Tuple.of(product.getId(), product.getProductName(), product.getPrice());
  }

  public static void fromJson(JsonObject json, Product obj) {

    if (json.getValue("id") instanceof String) {
      obj.setId(UUID.fromString((String) json.getValue("id")));
    }
    if (json.getValue("product_name") instanceof String) {
      obj.setProductName((String)json.getValue("product_name"));
    }
    if (json.getValue("price") instanceof Double) {
      obj.setPrice((double)json.getValue("price"));
    }
  }

  public static void toJson(Product obj, JsonObject json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId().toString());
    }
    if (obj.getProductName() != null) {
      json.put("product_name", obj.getProductName());
    }
    if (obj.getPrice() != 0) {
      json.put("price", obj.getPrice());
    }
  }
}
