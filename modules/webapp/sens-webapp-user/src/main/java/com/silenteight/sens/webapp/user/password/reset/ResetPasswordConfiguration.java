package com.silenteight.sens.webapp.user.password.reset;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResetPasswordConfiguration {

  @Bean
  ResetInternalUserPasswordUseCase resetInternalUserPasswordUseCase(
      TemporaryPasswordGenerator temporaryPasswordGenerator,
      UserCredentialsRepository userCredentialsRepository) {
    return new ResetInternalUserPasswordUseCase(
        userCredentialsRepository, temporaryPasswordGenerator);
  }
}
