package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;

import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AnalysisServiceApiProperties.class)
class AnalysisServiceApiConfiguration {

  private final AnalysisServiceApiProperties properties;

  @Profile("dev")
  @Bean
  AnalysisServiceApi analysisServiceApiMock() {
    return new AnalysisServiceApiMock();
  }

  @Primary
  @Profile("!dev")
  @Bean
  AnalysisServiceApi analysisServiceApi() {
    return new AnalysisServiceGrpcApi(
        analysisServiceBlockingStub(), properties.getDeadlineInSeconds());
  }

  private AnalysisServiceBlockingStub analysisServiceBlockingStub() {
    return AnalysisServiceGrpc.newBlockingStub(
        ManagedChannelBuilder.forTarget(properties.getGrpcAddress())
            .usePlaintext()
            .build())
        .withWaitForReady();
  }
}
