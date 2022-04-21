package com.vertx.shopping.services;

import com.vertx.shopping.common.verticle.BaseVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ProductService extends BaseVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

  private static final String SERVICE_NAME = "product-rest-api";

  private final ProductDBService productDBService;

  public ProductService(ProductDBService productDBService) {
    this.productDBService = productDBService;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();

    int port = 8091;

    Router router = Router.router(vertx);
    router.route("/product/*").handler(BodyHandler.create());
    router.get("/product/:id").handler(this::getDetails);
    router.get("/product").handler(this::getAll);
    createHttpServer(router, "localhost", port)
      .compose(serverCreated -> publishHttpEndpoint(SERVICE_NAME, "localhost", port));
  }

  private void getAll(RoutingContext routingContext) {
    productDBService.getListProduct(prepareResponse(routingContext));

  }

  private void getDetails(RoutingContext routingContext) {
    productDBService.getDetailsProduct(routingContext.request().getParam("id"), prepareResponse(routingContext));
  }

}
