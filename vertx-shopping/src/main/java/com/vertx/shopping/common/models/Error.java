package com.vertx.shopping.common.models;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Error {

  private int errorCode;

  private String errorMessage;

  private Map<String, String> details;

  public static Error internalServerError(String errorMessage) {
    return Error.builder().errorCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).errorMessage(errorMessage).build();
  }

  public static Error badRequest(String errorMessage) {
    return Error.builder().errorCode(HttpResponseStatus.BAD_REQUEST.code()).errorMessage(errorMessage).build();
  }



}
