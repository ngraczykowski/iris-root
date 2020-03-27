package com.silenteight.sens.webapp.user.password.reset;

import java.util.Optional;

public interface UserCredentialsRepository {

  Optional<ResettableUserCredentials> findUserCredentials(String username);
}
