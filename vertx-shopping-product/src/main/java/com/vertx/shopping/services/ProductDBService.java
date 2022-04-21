package com.vertx.shopping.services;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
@VertxGen
public interface ProductDBService {

  String SERVICE_NAME = "product-eb-service";

  String SERVICE_ADDRESS = "service.product";

  @Fluent
  ProductDBService initializePersistence();

  @Fluent
  ProductDBService getListProduct(Handler<AsyncResult<List<Product>>> resultHandler);

  @Fluent
  ProductDBService getDetailsProduct(String id, Handler<AsyncResult<Product>> resultHandler);

  static ProductDBService createProxy(Vertx vertx, String address) {
    return new ProductDBServiceVertxEBProxy(vertx, address);
  }

}
