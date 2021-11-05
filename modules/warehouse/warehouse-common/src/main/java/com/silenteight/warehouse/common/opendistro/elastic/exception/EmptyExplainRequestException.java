package com.silenteight.warehouse.common.opendistro.elastic.exception;

public class EmptyExplainRequestException extends RuntimeException {

  private static final long serialVersionUID = 2382500310684255841L;

  public EmptyExplainRequestException() {
    super("Es explain request returned empty or null");
  }
}
