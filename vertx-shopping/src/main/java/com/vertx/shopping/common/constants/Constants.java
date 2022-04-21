package com.vertx.shopping.common.constants;

public class Constants {

  public static final String SELECT_ID_DEFAULT = "SELECT [[ATTRIBUTE]] FROM [[TABLE]] WHERE id=?";

  public static final String SELECT_DEFAULT = "SELECT [[ATTRIBUTE]] FROM [[TABLE]]";

  public static final String WHERE = " WHERE [[CONDITIONAL]]";

  public static final String AND = " AND ";

  public static final String OR = " OR ";

  public static final String TABLE_TEMP = "[[TABLE]]";

  public static final String ATTRIBUTE_TEMP = "[[ATTRIBUTE]]";

  public static final String CONDITIONAL_TEMP = "[[CONDITIONAL]]";

  public static final String EQUAL_TEMP = "`[[PARAM]]=?`";

  public static final String PARAM_TEMP = "[[PARAM]]";

}
