package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.Getter;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;

public class OpendistroElasticClientException extends RuntimeException {

  private static final long serialVersionUID = -3893798708453715414L;

  @Getter
  private final int statusCode;

  @Getter
  private final String url;

  public OpendistroElasticClientException(
      int statusCode, String message, String url, String context, Exception e) {

    super("Error while calling " + context + " ES response: " + statusCode + ": " + message, e);
    this.statusCode = statusCode;
    this.url = url;
  }

  public boolean isNotFound() {
    return statusCode == SC_NOT_FOUND;
  }
}
