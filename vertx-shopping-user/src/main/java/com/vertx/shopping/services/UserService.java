package com.vertx.shopping.services;

import com.vertx.shopping.common.verticle.BaseVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class UserService extends BaseVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  private static final String SERVICE_NAME = "user-rest-api";

  private final UserDBService userDBService;

  public UserService(UserDBService userDBService) {
    this.userDBService = userDBService;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();

    int port = 8090;

    Router router = Router.router(vertx);
    router.route("/user/*").handler(BodyHandler.create());
    router.get("/user/:id").handler(this::getDetails);
    router.get("/user").handler(this::getAll);
    createHttpServer(router, "localhost", port)
      .compose(serverCreated -> publishHttpEndpoint(SERVICE_NAME, "localhost", port));
  }

  private void getAll(RoutingContext routingContext) {
    userDBService.getListUser(prepareResponse(routingContext));

  }

  private void getDetails(RoutingContext routingContext) {
    userDBService.getDetailsUser(routingContext.request().getParam("id"), prepareResponse(routingContext));
  }

}
