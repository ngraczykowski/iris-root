package com.silenteight.bridge.core.recommendation.infrastructure.grpc;

import com.silenteight.adjudication.api.library.v1.recommendation.RecommendationGrpcAdapter;
import com.silenteight.adjudication.api.library.v1.recommendation.RecommendationServiceClient;
import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.bridge.core.registration.infrastructure.util.KnownServices;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties({ RecommendationGrpcConfigurationProperties.class })
class RecommendationGrpcServiceConfiguration {

  @GrpcClient(KnownServices.ADJUDICATION_ENGINE)
  RecommendationServiceBlockingStub recommendationServiceBlockingStub;

  @Bean
  @Profile({ "dev", "test" })
  RecommendationServiceClient recommendationServiceMock() {
    return new RecommendationServiceClientMock();
  }

  @Bean
  @ConditionalOnMissingBean
  RecommendationServiceClient recommendationService(
      RecommendationGrpcConfigurationProperties properties) {
    return new RecommendationGrpcAdapter(
        recommendationServiceBlockingStub,
        properties.recommendationDeadline().getSeconds());
  }
}
