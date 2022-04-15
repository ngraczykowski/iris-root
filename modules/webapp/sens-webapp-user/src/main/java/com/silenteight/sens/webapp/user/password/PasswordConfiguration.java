package com.silenteight.sens.webapp.user.password;

import com.silenteight.sens.webapp.user.password.SensCompatiblePasswordGenerator.SensPasswordGeneratorConfig;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PasswordConfiguration {

  private static final int PASSWORD_LENGTH = 8;
  private static final int PASSWORD_NUMBERS_COUNT = 1;
  private static final int PASSWORD_LOWERCASE_LETTERS_COUNT = 1;

  @Bean
  SensCompatiblePasswordGenerator sensCompatiblePasswordGenerator() {
    return new SensCompatiblePasswordGenerator(
        new SensPasswordGeneratorConfig(
            PASSWORD_LENGTH, PASSWORD_NUMBERS_COUNT, PASSWORD_LOWERCASE_LETTERS_COUNT),
        RandomStringUtils::randomAlphanumeric,
        RandomStringUtils::randomAlphanumeric,
        RandomStringUtils::randomNumeric
    );
  }
}
