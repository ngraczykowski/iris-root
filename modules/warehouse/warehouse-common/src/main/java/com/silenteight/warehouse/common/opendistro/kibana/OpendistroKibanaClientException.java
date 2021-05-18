package com.silenteight.warehouse.common.opendistro.kibana;

public class OpendistroKibanaClientException extends RuntimeException {

  private static final long serialVersionUID = -5187094106880986538L;

  OpendistroKibanaClientException(String message) {
    super(message);
  }
}
