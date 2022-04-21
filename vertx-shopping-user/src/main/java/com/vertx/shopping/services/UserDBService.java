package com.vertx.shopping.services;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
@VertxGen
public interface UserDBService {

  String SERVICE_NAME = "user-eb-service";

  String SERVICE_ADDRESS = "service.user";

  @Fluent
  UserDBService getListUser(Handler<AsyncResult<List<User>>> resultHandler);

  @Fluent
  UserDBService getDetailsUser(String id, Handler<AsyncResult<User>> resultHandler);

  static UserDBService createProxy(Vertx vertx, String address) {
    return new UserDBServiceVertxEBProxy(vertx, address);
  }

}
