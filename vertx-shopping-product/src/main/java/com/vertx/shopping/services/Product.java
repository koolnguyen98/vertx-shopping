package com.vertx.shopping.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vertx.shopping.services.mapper.ProductMapper;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@DataObject(generateConverter = true)
public class Product {
  private UUID id;
  private String productName;
  private double price = 0.0d;

  public Product(JsonObject json) {
    ProductMapper.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ProductMapper.toJson(this, json);
    return json;
  }
}
