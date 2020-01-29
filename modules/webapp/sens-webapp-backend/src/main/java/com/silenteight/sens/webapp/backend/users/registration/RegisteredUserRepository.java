package com.silenteight.sens.webapp.backend.users.registration;

import com.silenteight.sens.webapp.backend.users.registration.domain.CompletedUserRegistration;

public interface RegisteredUserRepository {

  void save(CompletedUserRegistration userRegistration);
}
