package com.silenteight.hsbc.bridge.file;

public class ResourceDoesNotExistException extends RuntimeException {

  private static final long serialVersionUID = 3585347984151478688L;

  public ResourceDoesNotExistException(Throwable cause) {
    super(cause);
  }
}
