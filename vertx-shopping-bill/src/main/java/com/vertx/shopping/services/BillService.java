package com.vertx.shopping.services;

import com.vertx.shopping.common.verticle.BaseVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class BillService extends BaseVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(BillService.class);

  private static final String SERVICE_NAME = "bill-rest-api";

  private final BillDBService billDBService;

  public BillService(BillDBService billDBService) {
    this.billDBService = billDBService;
  }

  @Override
  public void start(Promise<Void> promise) throws Exception {
    super.start();

    int port = 8092;

    Router router = Router.router(vertx);
    router.route("/bill/*").handler(BodyHandler.create());
    router.get("/bill").handler(this::getAllBillForUser);
    router.get("/bill/:id").handler(this::getBillDetail);
    createHttpServer(router, "localhost", port)
      .compose(serverCreated -> publishHttpEndpoint(SERVICE_NAME, "localhost", port));
  }

  private void getAllBillForUser(RoutingContext routingContext) {
    String id = String.valueOf(routingContext.queryParam("userId"));
    billDBService.getAllBillForUser(id, prepareResponse(routingContext));
  }

  private void getBillDetail(RoutingContext routingContext) {
    String id = routingContext.pathParam("id");
    String userId = String.valueOf(routingContext.queryParam("userId"));
    billDBService.getBillDetail(id, userId, prepareResponse(routingContext));
  }

}
