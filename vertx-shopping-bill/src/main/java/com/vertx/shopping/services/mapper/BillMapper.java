package com.vertx.shopping.services.mapper;

import com.vertx.shopping.services.Bill;
import com.vertx.shopping.services.BillDetail;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BillMapper {

  public static Bill from(Row row) {
    UUID id = UUID.fromString(row.getString("id"));
    UUID userId = UUID.fromString(row.getString("user_id"));
    double amount = row.getDouble("amount");

    List<BillDetail> billDetails = new ArrayList<>();

    row.getJsonArray("bill_details").forEach(item -> {
      if (item instanceof JsonObject) {
        billDetails.add(new BillDetail((JsonObject) item));
      }
    });

    return new Bill(id, userId, amount, billDetails);
  }

  public static Tuple to(Bill bill) {
    return Tuple.of(bill.getId(), bill.getUserId(), bill.getAmount());
  }

  public static void fromJson(JsonObject json, Bill obj) {

    if (json.getValue("id") instanceof String) {
      obj.setId(UUID.fromString((String) json.getValue("id")));
    }
    if (json.getValue("user_id") instanceof String) {
      obj.setUserId(UUID.fromString((String)json.getValue("user_id")));
    }
    if (json.getValue("amount") instanceof Double) {
      obj.setAmount((double)json.getValue("amount"));
    }
    if (json.getValue("bill_details") instanceof JsonArray) {
      List<BillDetail> billDetails = new ArrayList<>();

      json.getJsonArray("bill_details").forEach(item -> {
        if (item instanceof JsonObject) {
          billDetails.add(new BillDetail((JsonObject) item));
        }
      });
    }
  }

  public static void toJson(Bill obj, JsonObject json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId().toString());
    }
    if (obj.getUserId() != null) {
      json.put("user_id", obj.getUserId());
    }
    if (obj.getAmount() != 0) {
      json.put("amount", obj.getAmount());
    }
    if (obj.getBillDetais() != null) {
      JsonArray array = new JsonArray();
      obj.getBillDetais().forEach(item -> array.add(item.toJson()));
      json.put("bill_details", array);
    }
  }

}
