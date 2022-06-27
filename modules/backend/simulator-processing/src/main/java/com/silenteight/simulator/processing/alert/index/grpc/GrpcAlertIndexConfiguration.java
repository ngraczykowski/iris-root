package com.silenteight.simulator.processing.alert.index.grpc;

import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcAlertIndexConfiguration {

  @GrpcClient("adjudication-engine")
  private RecommendationServiceBlockingStub recommendationServiceBlockingStub;

  @Bean
  GrpcRecommendationService grpcRecommendationService() {
    return new GrpcRecommendationService(recommendationServiceBlockingStub.withWaitForReady());
  }
}
