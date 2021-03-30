package com.silenteight.hsbc.bridge.analysis;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;

import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
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
    return new AnalysisServiceGrpcApi(analysisServiceBlockingStub());
  }

  private AnalysisServiceBlockingStub analysisServiceBlockingStub() {
    return AnalysisServiceGrpc.newBlockingStub(
        ManagedChannelBuilder.forTarget(properties.getGrpcAddress())
            .usePlaintext()
            .build())
        .withWaitForReady();
  }
}
