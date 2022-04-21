package com.vertx.shopping.services.mapper;

import com.vertx.shopping.services.Bill;
import com.vertx.shopping.services.BillDetail;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.UUID;

public class BillDetailMapper {

  public static BillDetail from(Row row) {
    UUID productId = UUID.fromString(row.getString("product_id"));
    String productName = row.getString("product_name");
    double price = row.getDouble("price");
    return new BillDetail(productId, productName, price);
  }

  public static Tuple to(BillDetail billDetail) {
    return Tuple.of(billDetail.getProductId(), billDetail.getProductName(), billDetail.getPrice());
  }

  public static void fromJson(JsonObject json, BillDetail obj) {

    if (json.getValue("product_id") instanceof String) {
      obj.setProductId(UUID.fromString((String) json.getValue("product_id")));
    }
    if (json.getValue("product_name") instanceof String) {
      obj.setProductName((String)json.getValue("product_name"));
    }
    if (json.getValue("price") instanceof Double) {
      obj.setPrice((double)json.getValue("price"));
    }
  }

  public static void toJson(BillDetail obj, JsonObject json) {
    if (obj.getProductId() != null) {
      json.put("product_id", obj.getProductId().toString());
    }
    if (obj.getProductName() != null) {
      json.put("product_name", obj.getProductName());
    }
    if (obj.getPrice() != 0) {
      json.put("price", obj.getPrice());
    }
  }

}
