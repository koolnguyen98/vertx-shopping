package com.vertx.shopping.services.impl;

import com.vertx.shopping.common.constants.Constants;
import com.vertx.shopping.common.db.dbs.DBConnection;
import com.vertx.shopping.services.Bill;
import com.vertx.shopping.services.BillDBService;
import com.vertx.shopping.services.mapper.BillMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BillDB extends DBConnection implements BillDBService {

  private static final String TABLE_NAME = "bill_tb";
  private static final String LIST_ATTRIBUTE = "id, user_id, amount, bill_details";

  public BillDB(Vertx vertx) {
    super(vertx);
  }

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

  public BillDBService getBillDetail(String id, String userId, Handler<AsyncResult<List<Bill>>> resultHandler) {
    StringBuilder query = new StringBuilder(Constants.SELECT_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                                                       .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE))
      .append(Constants.WHERE.replace(Constants.CONDITIONAL_TEMP, (listParamToConditionals(Arrays.asList("id", "user_id")))));
    Tuple tuple = Tuple.of(id, userId);
    this.getList(query.toString()).map(rowSet -> {
      List<Bill> bills = new ArrayList<>();
      rowSet.forEach(row -> bills.add(BillMapper.from(row)));
      return bills;
    }).onComplete(resultHandler);
    return this;
  }

}
