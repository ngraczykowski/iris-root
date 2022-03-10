package com.silenteight.connector.ftcc.ingest.adapter.outgoing;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AdapterOutgoingConfiguration {

  @Bean
  RegistrationApiClient registrationApiClient(
      RegistrationServiceClient registrationServiceClient) {

    return new RegistrationGrpcAdapter(registrationServiceClient);
  }
}
