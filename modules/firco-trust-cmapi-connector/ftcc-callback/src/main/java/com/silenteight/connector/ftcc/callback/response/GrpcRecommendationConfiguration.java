package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;

import com.silenteight.recommendation.api.library.v1.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.silenteight.connector.ftcc.common.resource.MessageResource.RESOURCE_NAME_PREFIX;
import static java.util.UUID.randomUUID;

@Configuration
class GrpcRecommendationConfiguration {

  @Profile("!mockcorebridge")
  @Bean
  RecommendationClientApi recommendationClientApi(
      RecommendationServiceClient recommendationServiceClient) {
    return new RecommendationGrpcAdapter(recommendationServiceClient);
  }

  @Profile("mockcorebridge")
  @Bean
  RecommendationClientApi mockRecommendationClientApi() {
    return (String analysisId) -> {
      var recommendationOuts = IntStream.range(0, 1)
          .mapToObj(alertId -> RecommendationOut.builder()
              .recommendedAt(OffsetDateTime.now())
              .recommendationComment("test comment")
              .recommendedAction("PTP")
              .alert(AlertOut
                  .builder()
                  .id(RESOURCE_NAME_PREFIX + randomUUID())
                  .build())
              .build())
          .collect(Collectors.toList());
      return RecommendationsOut.builder()
          .recommendations(recommendationOuts)
          .build();
    };
  }

  @RequiredArgsConstructor
  static class RecommendationGrpcAdapter implements RecommendationClientApi {

    private final RecommendationServiceClient recommendationServiceClient;

    @Override
    public RecommendationsOut recommendation(String analysisId) {
      return recommendationServiceClient.getRecommendations(
          RecommendationsIn.builder()
              .analysisName(analysisId)
              .build());
    }
  }
}
