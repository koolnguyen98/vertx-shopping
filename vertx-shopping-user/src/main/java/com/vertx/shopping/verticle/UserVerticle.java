package com.vertx.shopping.verticle;

import com.vertx.shopping.services.impl.UserDB;
import com.vertx.shopping.services.UserDBService;
import com.vertx.shopping.common.verticle.BaseVerticle;
import com.vertx.shopping.services.UserService;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

import static com.vertx.shopping.services.UserDBService.SERVICE_ADDRESS;
import static com.vertx.shopping.services.UserDBService.SERVICE_NAME;

public class UserVerticle extends BaseVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserVerticle.class);

  private UserDBService userDBService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();

    userDBService = new UserDB(vertx);

    new ServiceBinder(vertx).setAddress(SERVICE_ADDRESS)
                            .register(UserDBService.class, userDBService);

//    initDatabase(userDBService)
//      .compose(initDatabase ->
        publishEventBusService(SERVICE_NAME, SERVICE_ADDRESS, UserDBService.class)
      .compose(servicePublished -> deployRestVerticle());
  }

  private Future<Void> initDatabase(UserDBService service) {
    Promise<Void> promise = Promise.promise();
    Future<Void> initFuture = promise.future();
    service.initializePersistence();
    return promise.future().map(v -> null);
  }

  private Future<Void> deployRestVerticle() {
    Promise<Void> promise = Promise.promise();
    Future<Void> future = promise.future();
    vertx.deployVerticle(new UserService(userDBService));
    return future.map(r -> null);
  }

}
