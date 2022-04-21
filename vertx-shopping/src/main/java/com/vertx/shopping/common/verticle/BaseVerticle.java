package com.vertx.shopping.common.verticle;

import io.vertx.core.*;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;

import java.util.Optional;
import java.util.Set;

public class BaseVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseVerticle.class);

  protected ServiceDiscovery discovery;

  protected Set<Record> registeredRecords = new ConcurrentHashSet<>();

  @Override
  public void start() throws Exception {
    discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setAnnounceAddress("service-announce"));
  }

  protected  <T> Handler<AsyncResult<T>> prepareResponse(RoutingContext routingContext) {
    return ar -> {
      if(ar.succeeded()) {
        T res = ar.result();
        if (res != null) {
          routingContext
            .response()
            .putHeader("content-type", "application/json")
            .setStatusCode(200)
            .setStatusMessage("OK")
            .end(Optional.ofNullable(res).map(Json::encode).orElse("{}"));
        }
      } else {
        routingContext
          .response()
          .putHeader("content-type", "application/json")
          .setStatusCode(500)
          .setStatusMessage("OK")
          .end(Optional.ofNullable(ar.cause().getMessage()).map(Json::encode).orElse("{}"));
      }
    };

//    routingContext
//      .response()
//      .setStatusCode(code)
//      .setStatusMessage("OK")
//      .end(Optional.ofNullable(response).map(Json::encode).orElse("{}"));
  }

  protected Future<Void> publishEventBusService(String name, String address, Class serviceClass) {
    Record record = EventBusService.createRecord(name, address, serviceClass);
    return publish(record);
  }

  protected Future<Void> publishHttpEndpoint(String name, String host, int port) {
    Record record = HttpEndpoint.createRecord(name, host, port, "/",
      new JsonObject().put("api.name", config().getString("api.name", ""))
    );
    return publish(record);
  }


  private Future<Void> publish(Record record) {
    if (discovery == null) {
      try {
        start();
      } catch (Exception e) {
        throw new IllegalStateException("Cannot create discovery service");
      }
    }
    Promise<Void> promise = Promise.promise();
    // publish the service
    discovery.publish(record, ar -> {
      if (ar.succeeded()) {
        registeredRecords.add(record);
        LOGGER.info("Service <" + ar.result().getName() + "> published");
        promise.complete();
      } else {
        promise.fail(ar.cause());
      }
    });

    return promise.future();
  }

  protected Future<Void> createHttpServer(Router router, String host, int port) {
    Promise<HttpServer> promise = Promise.promise();
    Future<HttpServer> httpServerFuture = promise.future();
    vertx.createHttpServer()
         .requestHandler(router)
         .listen(port, host, res -> {
      if (res.succeeded()) {
        LOGGER.info("App Started");
        promise.complete();
      } else {
        LOGGER.error("Error while start server: ", res.cause());
        promise.fail(res.cause());
      }
    });
    return httpServerFuture.map(r -> null);
  }

}
