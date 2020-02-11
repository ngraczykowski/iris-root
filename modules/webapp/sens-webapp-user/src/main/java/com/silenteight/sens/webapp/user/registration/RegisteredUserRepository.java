package com.silenteight.sens.webapp.user.registration;

import com.silenteight.sens.webapp.user.registration.domain.CompletedUserRegistration;

public interface RegisteredUserRepository {

  void save(CompletedUserRegistration userRegistration);
}
