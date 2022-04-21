package com.vertx.shopping.services.mapper;

import com.vertx.shopping.services.User;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

import java.util.UUID;

public class UserMapper {

  public static User from(Row row) {
    UUID id = UUID.fromString(row.getString("id"));
    String userName = row.getString("user_name");
    return new User(id, userName);
  }

  public static Tuple to(User user) {
    return Tuple.of(user.getId(), user.getUserName());
  }

  public static void fromJson(JsonObject json, User obj) {

    if (json.getValue("id") instanceof String) {
      obj.setId(UUID.fromString((String) json.getValue("id")));
    }
    if (json.getValue("user_name") instanceof String) {
      obj.setUserName((String)json.getValue("user_name"));
    }
  }

  public static void toJson(User obj, JsonObject json) {
    if (obj.getId() != null) {
      json.put("id", obj.getId().toString());
    }
    if (obj.getUserName() != null) {
      json.put("user_name", obj.getUserName());
    }
  }
}
