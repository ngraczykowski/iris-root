package com.silenteight.sens.webapp.user.password;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResetPasswordConfiguration {

  private static final int TEMPORARY_PASSWORD_SIZE = 8;

  @Bean
  TemporaryPasswordGenerator temporaryPasswordGenerator() {
    return new TemporaryPasswordGenerator(
        TEMPORARY_PASSWORD_SIZE,
        RandomStringUtils::randomAlphanumeric);
  }

  @Bean
  ResetInternalUserPasswordUseCase resetInternalUserPasswordUseCase(
      TemporaryPasswordGenerator temporaryPasswordGenerator,
      UserCredentialsRepository userCredentialsRepository) {
    return new ResetInternalUserPasswordUseCase(
        userCredentialsRepository, temporaryPasswordGenerator);
  }
}
