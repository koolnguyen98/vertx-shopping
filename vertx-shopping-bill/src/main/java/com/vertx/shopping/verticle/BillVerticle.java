package com.vertx.shopping.verticle;

import com.vertx.shopping.common.verticle.BaseVerticle;
import com.vertx.shopping.services.Bill;
import com.vertx.shopping.services.BillDBService;
import com.vertx.shopping.services.BillService;
import com.vertx.shopping.services.impl.BillDB;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

import static com.vertx.shopping.services.BillDBService.SERVICE_ADDRESS;
import static com.vertx.shopping.services.BillDBService.SERVICE_NAME;

public class BillVerticle extends BaseVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(BillVerticle.class);

  private BillDBService billDBService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();

    billDBService = new BillDB(vertx);

    new ServiceBinder(vertx).setAddress(SERVICE_ADDRESS)
                            .register(BillDBService.class, billDBService);

//    initDatabase(billDBService)
//      .compose(initDatabase ->
    publishEventBusService(SERVICE_NAME, SERVICE_ADDRESS, BillDBService.class)
      .compose(servicePublished -> deployRestVerticle());
  }

  private Future<Void> initDatabase(BillDBService service) {
    Promise<Void> promise = Promise.promise();
    Future<Void> initFuture = promise.future();
    service.initializePersistence();
    return promise.future().map(v -> null);
  }

  private Future<Void> deployRestVerticle() {
    Promise<Void> promise = Promise.promise();
    Future<Void> future = promise.future();
    vertx.deployVerticle(new BillService(billDBService));
    return future.map(r -> null);
  }

}
