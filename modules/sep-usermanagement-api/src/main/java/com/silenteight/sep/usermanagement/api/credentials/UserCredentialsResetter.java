package com.silenteight.sep.usermanagement.api.credentials;

import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;

public interface UserCredentialsResetter {

  void reset(TemporaryPassword temporaryPassword);

  default boolean ownerIsNotInternal() {
    return !ownerIsInternal();
  }

  boolean ownerIsInternal();
}
