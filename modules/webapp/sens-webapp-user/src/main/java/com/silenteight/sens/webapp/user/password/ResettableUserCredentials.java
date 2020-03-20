package com.silenteight.sens.webapp.user.password;

public interface ResettableUserCredentials {

  void reset(TemporaryPassword temporaryPassword);

  boolean ownerIsInternal();

  default boolean ownerIsNotInternal() {
    return !ownerIsInternal();
  }
}
