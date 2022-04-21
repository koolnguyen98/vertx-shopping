package com.vertx.shopping.services.impl;

import com.vertx.shopping.common.constants.Constants;
import com.vertx.shopping.common.db.dbs.DBConnection;
import com.vertx.shopping.services.Bill;
import com.vertx.shopping.services.BillDBService;
import com.vertx.shopping.services.mapper.BillMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillDB extends DBConnection implements BillDBService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BillDB.class);

  private static final String TABLE_NAME = "bill_tb";
  private static final String LIST_ATTRIBUTE = "id, user_id, amount, bill_details";

  public BillDB(Vertx vertx) {
    super(vertx);
  }

  @Override
  public BillDBService initializePersistence() {
    pool.withTransaction(sqlCLient -> sqlCLient.query(CREATE_STATEMENT)
                                               .execute()
                                               .compose(res -> sqlCLient.query(INSERT_STATEMENT).execute())
                                               .onComplete(res -> {
                                                 if (res.succeeded()) {
                                                   LOGGER.info("Init table Bill successfully");
                                                 } else {
                                                   LOGGER.error("Error while initialize persistence: ", res.cause());
                                                 }
                                                 sqlCLient.close();
                                               }));
    return this;
  }

  @Override
  public BillDBService getAllBillForUser(String userId, Handler<AsyncResult<List<Bill>>> resultHandler) {
    StringBuilder query = new StringBuilder(Constants.SELECT_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                                                    .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE))
      .append(Constants.WHERE.replace(Constants.CONDITIONAL_TEMP, (listParamToConditionals(Arrays.asList("user_id")))));

    Tuple tuple = Tuple.of(userId);
    this.getList(tuple, query.toString()).map(rowSet -> {
      List<Bill> bills = new ArrayList<>();
      rowSet.forEach(row -> bills.add(BillMapper.from(row)));
      return bills;
    }).onComplete(resultHandler);
    return this;
  }

  @Override
  public BillDBService getBillDetail(String id, String userId, Handler<AsyncResult<List<Bill>>> resultHandler) {
    StringBuilder query = new StringBuilder(Constants.SELECT_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                                                    .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE))
      .append(Constants.WHERE.replace(Constants.CONDITIONAL_TEMP, (listParamToConditionals(Arrays.asList("id")))));

    Tuple tuple = Tuple.of(id);
    this.getList(tuple, query.toString()).map(rowSet -> {
      List<Bill> bills = new ArrayList<>();
      rowSet.forEach(row -> bills.add(BillMapper.from(row)));
      return bills;
    }).onComplete(resultHandler);
    return this;
  }

  private static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS `bill_tb` (\n" +
    "  `id` varchar(50) NOT NULL,\n" +
    "  `user_id` varchar(50) NOT NULL,\n" +
    "  `amount` double NOT NULL,\n" +
    "  `bill_details` LONGTEXT NOT NULL,\n" +
    "  PRIMARY KEY (`id`) )";

  private static final String INSERT_STATEMENT = "INSERT INTO bill_tb (id, user_id, amount, bill_details) VALUES ('997c3547-1d09-40e4-bd5c-a1a8908b7389', 'ea4edc44-e42e-4908-ae40-d3b4dc8bd6a4', 25000, '\"[{\"product_id\":\"a6a019f6-ab3f-4166-8967-d31d00705f29\",\"product_name\":\"Bear\",\"price\":25000}]\"')";



}
