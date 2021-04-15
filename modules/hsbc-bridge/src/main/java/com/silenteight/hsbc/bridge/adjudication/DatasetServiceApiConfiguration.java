package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DatasetServiceApiProperties.class)
class DatasetServiceApiConfiguration {

  private final DatasetServiceApiProperties datasetServiceApiProperties;

  @Profile("!dev")
  @Bean
  DatasetServiceApi datasetServiceApiGrpc() {
    return new DatasetServiceApiGrpc(
        datasetServiceBlockingStub(), datasetServiceApiProperties.getDeadlineInSeconds());
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
