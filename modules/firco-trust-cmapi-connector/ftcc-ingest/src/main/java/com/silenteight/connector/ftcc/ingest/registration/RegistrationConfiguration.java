package com.silenteight.connector.ftcc.ingest.registration;

import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RegistrationConfiguration {

  @Bean
  RegistrationService registrationService(RegistrationServiceClient registrationServiceClient) {
    return new RegistrationService(registrationServiceClient);
  }
}
