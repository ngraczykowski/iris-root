package com.silenteight.sens.webapp.backend.user.registration;

import com.silenteight.sens.webapp.backend.user.registration.domain.CompletedUserRegistration;

public interface RegisteredUserRepository {

  void save(CompletedUserRegistration userRegistration);
}
