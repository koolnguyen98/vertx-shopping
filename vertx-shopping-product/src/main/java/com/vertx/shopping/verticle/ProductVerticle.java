package com.vertx.shopping.verticle;

import com.vertx.shopping.common.verticle.BaseVerticle;
import com.vertx.shopping.services.ProductDBService;
import com.vertx.shopping.services.ProductService;
import com.vertx.shopping.services.impl.ProductDB;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

import static com.vertx.shopping.services.ProductDBService.SERVICE_ADDRESS;
import static com.vertx.shopping.services.ProductDBService.SERVICE_NAME;

public class ProductVerticle extends BaseVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductVerticle.class);

  private ProductDBService productDBService;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();

    productDBService = new ProductDB(vertx);

    new ServiceBinder(vertx).setAddress(SERVICE_ADDRESS)
                            .register(ProductDBService.class, productDBService);

//    initDatabase(productDBService)
//      .compose(initDatabase ->
        publishEventBusService(SERVICE_NAME, SERVICE_ADDRESS, ProductDBService.class)
      .compose(servicePublished -> deployRestVerticle());
  }

  private Future<Void> initDatabase(ProductDBService service) {
    Promise<Void> promise = Promise.promise();
    Future<Void> initFuture = promise.future();
    service.initializePersistence();
    return promise.future().map(v -> null);
  }

  private Future<Void> deployRestVerticle() {
    Promise<Void> promise = Promise.promise();
    Future<Void> future = promise.future();
    vertx.deployVerticle(new ProductService(productDBService));
    return future.map(r -> null);
  }

}
