package com.vertx.shopping.services;

import com.vertx.shopping.services.mapper.BillDetailMapper;
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
public class BillDetail {

  private UUID productId;

  private String productName;

  private double price = 0.0d;

  public BillDetail(JsonObject json) {
    BillDetailMapper.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    BillDetailMapper.toJson(this, json);
    return json;
  }
}
