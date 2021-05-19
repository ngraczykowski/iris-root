package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Getter;

public class OpendistroKibanaClientException extends RuntimeException {

  private static final long serialVersionUID = -5187094106880986538L;

  OpendistroKibanaClientException(int statusCode, String message) {
    super(statusCode + ": " + message);
    this.statusCode = statusCode;
  }

  @Getter
  private final int statusCode;
}
