package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;

import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class AlertServiceApiConfiguration {

  private final AlertServiceApiProperties alertServiceApiProperties;

  @Bean
  @Profile("!dev")
  AlertServiceApi alertServiceGrpcApi() {
    return new AlertServiceGrpcApi(alertServiceBlockingStub());
  }

  @Bean
  @Profile("dev")
  AlertServiceApi alertServiceGrpcMock() {
    return new AlertServiceApiMock();
  }

  private AlertServiceBlockingStub alertServiceBlockingStub() {
    var channel = ManagedChannelBuilder.forTarget(
        alertServiceApiProperties.getGrpcAddress())
        .usePlaintext()
        .build();

    return AlertServiceGrpc.newBlockingStub(channel).withWaitForReady();
  }
}
