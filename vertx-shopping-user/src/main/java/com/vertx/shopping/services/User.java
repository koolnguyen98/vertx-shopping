package com.vertx.shopping.services;

import com.vertx.shopping.services.mapper.UserMapper;
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
public class User {
  private UUID id;
  private String userName;

  public User(JsonObject json) {
    UserMapper.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    UserMapper.toJson(this, json);
    return json;
  }

}
