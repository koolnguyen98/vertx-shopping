package com.vertx.shopping.services;

import com.vertx.shopping.services.mapper.BillMapper;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Setter
@Getter
@DataObject(generateConverter = true)
public class Bill {

  private UUID id;

  private UUID userId;

  private double amount = 0.0d;

  private List<BillDetail> billDetais = new ArrayList<>();

  public Bill(JsonObject json) {
    BillMapper.fromJson(json, this);
    if (json.getValue("billDetais") instanceof String) {
      this.billDetais = new JsonArray(json.getString("billDetais"))
        .stream()
        .map(e -> (JsonObject) e)
        .map(BillDetail::new)
        .collect(Collectors.toList());
    }
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    BillMapper.toJson(this, json);
    return json;
  }

}
