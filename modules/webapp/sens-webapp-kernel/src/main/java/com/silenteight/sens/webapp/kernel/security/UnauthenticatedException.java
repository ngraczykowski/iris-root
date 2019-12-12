package com.silenteight.sens.webapp.kernel.security;

public class UnauthenticatedException extends RuntimeException {

  private static final long serialVersionUID = 1697031309397549330L;

  UnauthenticatedException() {
    super("No user is currently authenticated");
  }
}

