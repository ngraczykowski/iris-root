package com.silenteight.simulator.processing.alert.index.grpc;

import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcAlertIndexConfiguration {

  @Bean
  GrpcRecommendationService grpcRecommendationService(
      @Qualifier("adjudication-engine") Channel channel) {

    return new GrpcRecommendationService(
        RecommendationServiceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
