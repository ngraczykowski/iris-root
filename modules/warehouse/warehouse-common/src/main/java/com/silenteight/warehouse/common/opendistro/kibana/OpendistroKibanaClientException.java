package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Getter;

public class OpendistroKibanaClientException extends RuntimeException {

  private static final long serialVersionUID = -5187094106880986538L;

  @Getter
  private final int statusCode;

  @Getter
  private final String url;

  public OpendistroKibanaClientException(int statusCode, String message, String url) {
    super(statusCode + ": " + message);
    this.statusCode = statusCode;
    this.url = url;
  }
}
