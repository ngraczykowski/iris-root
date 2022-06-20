/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.infrastructure.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;
import com.silenteight.registration.api.library.v1.RegistrationServiceGrpcAdapter;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.silenteight.iris.bridge.scb.ingest.infrastructure.util.KnownServices.REGISTRATION;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RegistrationGrpcConfigurationProperties.class)
class RegistrationGrpcServiceConfiguration {

  @GrpcClient(REGISTRATION)
  RegistrationServiceBlockingStub registrationServiceBlockingStub;

  @Bean
  @Profile("!dev")
  RegistrationServiceClient registrationServiceClientGrpcApi(
      RegistrationGrpcConfigurationProperties grpcProperties) {
    return new RegistrationServiceGrpcAdapter(
        registrationServiceBlockingStub, grpcProperties.registrationDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  RegistrationServiceClient registrationServiceClientMock() {
    return new RegistrationServiceClientMock();
  }
}
