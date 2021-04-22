package com.silenteight.warehouse.common.opendistro.elastic;

public class OpendistroElasticClientException extends RuntimeException {

  private static final long serialVersionUID = -3893798708453715414L;

  OpendistroElasticClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
