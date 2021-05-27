package com.silenteight.adjudication.engine.common.resource;

public class InvalidResourceNameException extends IllegalStateException {

  private static final long serialVersionUID = -7883493819740987068L;

  InvalidResourceNameException(String message) {
    super(message);
  }
}
