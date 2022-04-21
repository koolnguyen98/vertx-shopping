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
public interface BillDBService {

  String SERVICE_NAME = "bill-eb-service";

  String SERVICE_ADDRESS = "service.bill";

  @Fluent
  BillDBService getAllBillForUser(String userID, Handler<AsyncResult<List<Bill>>> resultHandler);

  @Fluent
  BillDBService getBillDetail(String id, String userId, Handler<AsyncResult<List<Bill>>> resultHandler);

  static BillDBService createProxy(Vertx vertx, String address) {
    return new BillDBServiceVertxEBProxy(vertx, address);
  }

}
