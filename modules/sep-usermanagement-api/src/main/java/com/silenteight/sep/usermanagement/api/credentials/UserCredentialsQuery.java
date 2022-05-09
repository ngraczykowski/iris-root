package com.silenteight.sep.usermanagement.api.credentials;

import lombok.NonNull;

import java.util.Optional;

public interface UserCredentialsQuery {

  Optional<UserCredentialsResetter> findByUsername(@NonNull String username);
}
