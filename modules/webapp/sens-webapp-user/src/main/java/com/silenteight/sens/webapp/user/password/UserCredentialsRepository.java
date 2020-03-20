package com.silenteight.sens.webapp.user.password;

import java.util.Optional;

public interface UserCredentialsRepository {

  Optional<ResettableUserCredentials> findUserCredentials(String username);
}
