package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
class DatasetServiceApiConfiguration {

  private final DatasetServiceApiProperties datasetServiceApiProperties;

  @Profile("!dev")
  @Bean
  DatasetServiceApi datasetServiceApiGrpc() {
    return new DatasetServiceApiGrpc(datasetServiceBlockingStub());
  }

  @Profile("dev")
  @Bean
  DatasetServiceApi datasetServiceApiMock() {
    return new DatasetServiceApiMock();
  }

  private DatasetServiceBlockingStub datasetServiceBlockingStub() {
    var channel = ManagedChannelBuilder.forTarget(datasetServiceApiProperties.getGrpcAddress())
        .usePlaintext()
        .build();

    return DatasetServiceGrpc.newBlockingStub(channel).withWaitForReady();
  }
}
