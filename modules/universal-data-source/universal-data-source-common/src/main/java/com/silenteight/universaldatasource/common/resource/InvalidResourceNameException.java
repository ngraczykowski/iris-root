package com.silenteight.universaldatasource.common.resource;

public class InvalidResourceNameException extends IllegalStateException {

  private static final long serialVersionUID = -7773217452456973689L;

  InvalidResourceNameException(String message) {
    super(message);
  }
}
