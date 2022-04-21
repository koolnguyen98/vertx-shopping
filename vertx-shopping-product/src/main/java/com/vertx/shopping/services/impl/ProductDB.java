package com.vertx.shopping.services.impl;

import com.vertx.shopping.common.constants.Constants;
import com.vertx.shopping.common.db.dbs.DBConnection;
import com.vertx.shopping.services.Product;
import com.vertx.shopping.services.ProductDBService;
import com.vertx.shopping.services.mapper.ProductMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductDB extends DBConnection implements ProductDBService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProductDB.class);

  private static final String TABLE_NAME = "product_tb";
  private static final String LIST_ATTRIBUTE = "id, product_name, price";

  public ProductDB(Vertx vertx) {
    super(vertx);
  }

  @Override
  public ProductDBService initializePersistence() {
    pool.withTransaction(sqlCLient -> sqlCLient.query(CREATE_STATEMENT)
                                               .execute()
                                               .compose(res -> sqlCLient.query(INSERT_STATEMENT).execute())
                                               .onComplete(res -> {
                                                 if (res.succeeded()) {
                                                   LOGGER.info("Init table Product successfully");
                                                 } else {
                                                   LOGGER.error("Error while initialize persistence: ", res.cause());
                                                 }
                                                 sqlCLient.close();
                                               }));
    return this;
  }

  @Override
  public ProductDBService getListProduct(Handler<AsyncResult<List<Product>>> resultHandler) {
    String query = Constants.SELECT_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                           .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE);
    this.getList(query).map(rowSet -> {
      List<Product> products = new ArrayList<>();
      rowSet.forEach(row -> products.add(ProductMapper.from(row)));
      return products;
    }).onComplete(resultHandler);
    return this;
  }

  @Override
  public ProductDBService getDetailsProduct(String id, Handler<AsyncResult<Product>> resultHandler) {
    String query = Constants.SELECT_ID_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                              .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE);
    this.getDetails(id, query).map(option -> ProductMapper.from(option.get())).onComplete(resultHandler);
    return this;
  }

  private static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS `product_tb` (\n" +
    "  `id` varchar(50) NOT NULL,\n" +
    "  `product_name` varchar(30) NOT NULL,\n" +
    "  `price` double NOT NULL,\n" +
    "  PRIMARY KEY (`id`) )";

  private static final String INSERT_STATEMENT = "INSERT INTO product_tb (id, product_name, price) VALUES ('a6a019f6-ab3f-4166-8967-d31d00705f29', 'Bear', 25000), ('f897b593-cd0d-4bed-a78c-7e9cc80c422d', 'CocaCola', 10000)";

}
