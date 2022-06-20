/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.grpc;

import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceGrpcAdapter;
import com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure.util.KnownServices;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(RecommendationGrpcConfigurationProperties.class)
class RecommendationGrpcServiceConfiguration {

  @GrpcClient(KnownServices.RECOMMENDATION)
  RecommendationServiceBlockingStub recommendationServiceBlockingStub;

  @Bean
  @Profile("!dev")
  @ConditionalOnMissingBean
  RecommendationServiceClient recommendationServiceClient(
      RecommendationGrpcConfigurationProperties grpcProperties) {
    return new RecommendationServiceGrpcAdapter(
        recommendationServiceBlockingStub,
        grpcProperties.recommendationDeadline().getSeconds());
  }

  @Bean
  @Profile("dev")
  RecommendationServiceClient recommendationServiceClientMock() {
    return new RecommendationServiceClientMock();
  }
}
