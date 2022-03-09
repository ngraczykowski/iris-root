package com.silenteight.connector.ftcc.app.grpc;

import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;
import com.silenteight.registration.api.library.v1.RegistrationServiceGrpcAdapter;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcConfiguration {

  private static final long DEADLINE_IN_SECONDS = 30L;

  @GrpcClient("core-bridge")
  RegistrationServiceBlockingStub registrationServiceBlockingStub;

  @Bean
  RegistrationServiceClient registrationServiceClient() {
    return new RegistrationServiceGrpcAdapter(registrationServiceBlockingStub, DEADLINE_IN_SECONDS);
  }
}
