package com.vertx.shopping.services.impl;

import com.vertx.shopping.common.constants.Constants;
import com.vertx.shopping.common.db.dbs.DBConnection;
import com.vertx.shopping.services.Product;
import com.vertx.shopping.services.ProductDBService;
import com.vertx.shopping.services.mapper.ProductMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import java.util.ArrayList;
import java.util.List;

public class ProductDB extends DBConnection implements ProductDBService {

  private static final String TABLE_NAME = "product_tb";
  private static final String LIST_ATTRIBUTE = "id, product_name, price";

  public ProductDB(Vertx vertx) {
    super(vertx);
  }

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

  public ProductDBService getDetailsProduct(String id, Handler<AsyncResult<Product>> resultHandler) {
    String query = Constants.SELECT_ID_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                              .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE);
    this.getDetails(id, query).map(option -> ProductMapper.from(option.get())).onComplete(resultHandler);
    return this;
  }

}
