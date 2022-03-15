package com.silenteight.connector.ftcc.ingest.adapter.outgoing.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
class GrpcOutgoingConfiguration {

  @Profile("!mockcorebridge")
  @Bean
  RegistrationApiClient registrationApiClient(
      RegistrationServiceClient registrationServiceClient) {

    return new RegistrationGrpcAdapter(registrationServiceClient);
  }

  @Profile("mockcorebridge")
  @Bean
  RegistrationApiClient mockRegistrationApiClient() {
    return batch -> log.info("Batch registered {}", batch.getBatchId());
  }
}
