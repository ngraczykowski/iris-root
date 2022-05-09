package com.silenteight.sep.usermanagement.api.user;

import lombok.NonNull;

import com.silenteight.sep.usermanagement.api.error.SimpleUserDomainError;

import java.util.Optional;

public interface UsernameUniquenessValidator {

  Optional<UsernameNotUniqueError> validate(@NonNull String username);

  class UsernameNotUniqueError extends SimpleUserDomainError {

    private static final long serialVersionUID = 2443691316375936859L;

    public UsernameNotUniqueError(String username) {
      super("Username " + username + " is not unique");
    }
  }
}
