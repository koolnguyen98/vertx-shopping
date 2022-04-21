package com.vertx.shopping.services.impl;

import com.vertx.shopping.common.constants.Constants;
import com.vertx.shopping.common.db.dbs.DBConnection;
import com.vertx.shopping.services.UserDBService;
import com.vertx.shopping.services.mapper.UserMapper;
import com.vertx.shopping.services.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserDB extends DBConnection implements UserDBService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserDB.class);

  private static final String TABLE_NAME = "user_tb";
  private static final String LIST_ATTRIBUTE = "id, user_name";

  public UserDB(Vertx vertx) {
    super(vertx);
  }

  @Override
  public UserDBService initializePersistence() {
    pool.withTransaction(sqlCLient -> sqlCLient.query(CREATE_STATEMENT)
                                               .execute()
                                               .compose(res -> sqlCLient.query(INSERT_STATEMENT).execute())
                                               .onComplete(res -> {
                                                 if (res.succeeded()) {
                                                   LOGGER.info("Init table User successfully");
                                                 } else {
                                                   LOGGER.error("Error while initialize persistence: ", res.cause());
                                                 }
                                                 sqlCLient.close();
                                               }));
    return this;
  }

  @Override
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

  @Override
  public UserDBService getDetailsUser(String id, Handler<AsyncResult<User>> resultHandler) {
    String query = Constants.SELECT_ID_DEFAULT.replace(Constants.TABLE_TEMP, TABLE_NAME)
                                              .replace(Constants.ATTRIBUTE_TEMP, LIST_ATTRIBUTE);
    this.getDetails(id, query).map(option -> UserMapper.from(option.get())).onComplete(resultHandler);
    return this;
  }

  private static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS `user_tb` (\n" +
    "  `id` varchar(50) NOT NULL,\n" +
    "  `user_name` varchar(20) NOT NULL,\n" +
    "  PRIMARY KEY (`id`),\n" +
    "  UNIQUE KEY `user_name_UNIQUE` (`user_name`) )";

  private static final String INSERT_STATEMENT = "INSERT INTO user_tb (id, user_name) VALUES ('f5684485-04b1-4cfc-8627-0efa9b914c38', 'thiennguyen'), ('ea4edc44-e42e-4908-ae40-d3b4dc8bd6a4', 'kietnguyen')";


}
