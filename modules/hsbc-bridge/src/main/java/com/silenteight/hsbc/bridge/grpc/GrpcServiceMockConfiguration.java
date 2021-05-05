package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.adjudication.DatasetServiceClient;
import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.transfer.TransferServiceClient;

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
  AlertServiceClientMock alertServiceApiMock() {
    return new AlertServiceClientMock();
  }

  @Bean
  DatasetServiceClient datasetServiceApiMock() {
    return new DatasetServiceClientMock();
  }

  @Bean
  ModelServiceClient modelServiceApiMock() {
    return new ModelServiceClientMock();
  }

  @Bean
  TransferServiceClient transferServiceApiMock() {
    return new TransferModelServiceClientMock();
  }
}
