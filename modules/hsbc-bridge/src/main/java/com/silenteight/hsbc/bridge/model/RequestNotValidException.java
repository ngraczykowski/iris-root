package com.silenteight.hsbc.bridge.model;

class RequestNotValidException extends RuntimeException {

  private static final long serialVersionUID = -5952313234321485573L;

  public RequestNotValidException(String name) {
    super("Request URI: " + name + " is not valid.");
  }
}
