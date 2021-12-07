package com.silenteight.registration.api.library.v1;

public final class EmptyOut {

  private static final EmptyOut INSTANCE = new EmptyOut();

  private EmptyOut() {
  }

  public static EmptyOut getInstance() {
    return INSTANCE;
  }
}
