package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.adjudication.DatasetServiceClient;
import com.silenteight.hsbc.bridge.transfer.TransferClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
class GrpcServiceMockConfiguration {

  @Bean
  AnalysisServiceClientMock analysisServiceApiMock() {
    return new AnalysisServiceClientMock();
  }

  @Bean
  RecommendationServiceClientMock recommendationServiceApiMock() {
    return new RecommendationServiceClientMock();
  }

  @Bean
  AlertServiceClientMock alertServiceGrpcMock() {
    return new AlertServiceClientMock();
  }

  @Bean
  DatasetServiceClient datasetServiceApiMock() {
    return new DatasetServiceClientMock();
  }

  @Bean
  TransferClient transferModelGrpcMock() {
    return new TransferClientMock();
  }
}
