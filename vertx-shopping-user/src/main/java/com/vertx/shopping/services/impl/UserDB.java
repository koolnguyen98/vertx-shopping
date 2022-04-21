package com.vertx.shopping.services.impl;

import com.vertx.shopping.common.constants.Constants;
import com.vertx.shopping.common.db.dbs.DBConnection;
import com.vertx.shopping.services.UserDBService;
import com.vertx.shopping.services.mapper.UserMapper;
import com.vertx.shopping.services.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.ArrayList;
import java.util.List;

public class UserDB extends DBConnection implements UserDBService {

  private static final String TABLE_NAME = "user_tb";
  private static final String LIST_ATTRIBUTE = "id, user_name";

  public UserDB(Vertx vertx) {
    super(vertx);
  }

  public UserDBService getListUser(Handler<AsyncResult<List<User>>> resultHandler) {
    String query = Constants.SELECT_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                           .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE);
    this.getList(query).map(rowSet -> {
      List<User> users = new ArrayList<>();
      rowSet.forEach(row -> users.add(UserMapper.from(row)));
      return users;
    }).onComplete(resultHandler);
    return this;
  }

  public UserDBService getDetailsUser(String id, Handler<AsyncResult<User>> resultHandler) {
    String query = Constants.SELECT_ID_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                              .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE);
    this.getDetails(id, query).map(option -> UserMapper.from(option.get())).onComplete(resultHandler);
    return this;
  }

}
