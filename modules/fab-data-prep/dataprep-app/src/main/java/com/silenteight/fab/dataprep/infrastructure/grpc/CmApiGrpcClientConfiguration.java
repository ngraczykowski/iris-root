package com.silenteight.fab.dataprep.infrastructure.grpc;

import com.silenteight.proto.fab.api.v1.AlertDetailsServiceGrpc.AlertDetailsServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.silenteight.fab.dataprep.infrastructure.grpc.KnownServices.CM_API_CONNECTOR;

@Configuration
class CmApiGrpcClientConfiguration {

  @GrpcClient(CM_API_CONNECTOR)
  private AlertDetailsServiceBlockingStub alertDetailsServiceBlockingStub;

  @Bean
  @Profile("!dev")
  AlertDetailsServiceClient alertsDetailsServiceClient() {
    return new AlertDetailsServiceClient(alertDetailsServiceBlockingStub);
  }

  @Bean
  @Profile("dev")
  AlertDetailsServiceClient alertsDetailsServiceClientMock() {
    return new AlertDetailsServiceClientMock();
  }
}
