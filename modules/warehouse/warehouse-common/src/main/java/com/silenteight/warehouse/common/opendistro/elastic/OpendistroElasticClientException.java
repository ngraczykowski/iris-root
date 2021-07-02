package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.Getter;

public class OpendistroElasticClientException extends RuntimeException {

  private static final long serialVersionUID = -3893798708453715414L;

  @Getter
  private final int statusCode;

  @Getter
  private final String url;

  public OpendistroElasticClientException(
      int statusCode, String message, String url, String context) {

    super("Error while calling " + context + " ES response: " + statusCode + ": " + message);
    this.statusCode = statusCode;
    this.url = url;
  }
}
