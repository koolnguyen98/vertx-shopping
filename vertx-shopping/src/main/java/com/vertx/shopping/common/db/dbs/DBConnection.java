package com.vertx.shopping.common.db.dbs;

import com.vertx.shopping.common.constants.Constants;
import com.vertx.shopping.common.models.Error;
import com.vertx.shopping.common.models.ListResponse;
import com.vertx.shopping.common.models.Response;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.*;

import java.util.*;

public class DBConnection {

  private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);

  private static final String userName = "admin";

  private static final int port = 3306;

  private static final String host = "localhost";

  private static final String secret = "1234@Abcd";

  private static final String database = "vertx-demo";

  protected static MySQLPool pool;

  public DBConnection(Vertx vertx) {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setPort(port)
      .setHost(host)
      .setDatabase(database)
      .setUser(userName)
      .setPassword(secret);

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
    this.pool = MySQLPool.pool(vertx, connectOptions, poolOptions);
  }

  protected Future<RowSet<Row>> getList(String sql) {
    Promise<RowSet<Row>> promise = Promise.promise();
    pool.getConnection(resp -> {
      if (resp.succeeded()) {
        SqlConnection conn = resp.result();
        LOGGER.info(sql);
        conn.query(sql).execute(queryResp -> {
          if (queryResp.succeeded()) {
            promise.complete(queryResp.result());
          } else {
            promise.fail(queryResp.cause());
          }
        });
        conn.close();
      } else {
        LOGGER.error("Error while get list user: ", resp.cause());
        promise.fail(resp.cause());
      }
    });

    return promise.future();
  }

  protected Future<RowSet<Row>> getList(Tuple tuple, String sql) {
    Promise<RowSet<Row>> promise = Promise.promise();
    pool.getConnection(resp -> {
      if (resp.succeeded()) {
        SqlConnection conn = resp.result();
        LOGGER.info(sql);
        conn.preparedQuery(sql).execute(tuple, queryResp -> {
          if (queryResp.succeeded()) {
            promise.complete(queryResp.result());
          } else {
            promise.fail(queryResp.cause());
          }
        });
        conn.close();
      } else {
        LOGGER.error("Error while get list user: ", resp.cause());
        promise.fail(resp.cause());
      }
    });

    return promise.future();
  }

  protected Future<Optional<Row>> getDetails(String id, String sql) {
    Promise<Optional<Row>> promise = Promise.promise();
    pool.getConnection(resp -> {
      if (resp.succeeded()) {
        SqlConnection conn = resp.result();
        LOGGER.info(sql);
        conn.preparedQuery(sql)
            .execute(Tuple.of(UUID.fromString(id)), queryResp -> {
              if (queryResp.succeeded()) {
                RowSet<Row> rows = queryResp.result();
                if (rows == null || rows.size() == 0) {
                  promise.complete(Optional.empty());
                } else {

                  rows.forEach(row -> promise.complete(Optional.of(row)));
                }
              } else {
                LOGGER.error("Error while get user: ", queryResp.cause());
                promise.fail(queryResp.cause());
              }
            });
        conn.close();
      } else {
        LOGGER.error("Error while get user: ", resp.cause());
        promise.fail(resp.cause());
      }
    });
    return promise.future();
  }

  protected String listParamToConditionals(List<String> listConditions) {
    StringBuilder conditional = new StringBuilder();

    listConditions.forEach(ar -> conditional.append(Constants.EQUAL_TEMP.replace(Constants.PARAM_TEMP, ar)));

    return conditional.toString();
  }

}
