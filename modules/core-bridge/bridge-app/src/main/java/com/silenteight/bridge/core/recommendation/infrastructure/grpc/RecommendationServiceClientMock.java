package com.silenteight.bridge.core.recommendation.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.recommendation.*;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
class RecommendationServiceClientMock implements RecommendationServiceClient {

  @Override
  public Collection<RecommendationWithMetadataOut> getRecommendations(
      @NotNull String analysis) {
    log.info("MOCK: Get recommendations from AE");
    return List.of(createRecommendation(), createRecommendation());
  }

  private RecommendationWithMetadataOut createRecommendation() {
    return RecommendationWithMetadataOut.builder()
        .alert("alert/mock" + UUID.randomUUID())
        .name("name/mock" + UUID.randomUUID())
        .recommendedAction("recommendationAction/mock")
        .recommendationComment("recommendationComment/mock")
        .s8recommendedAction("s8recommendationAction/mock")
        .date(OffsetDateTime.now())
        .metadata(createMockMetadata())
        .build();
  }

  private RecommendationMetadataOut createMockMetadata() {
    var recommendationMetadata = new RecommendationMetadataOut();
    var metadata = new MatchMetadataOut();
    metadata.setMatch("match/mock" + UUID.randomUUID());
    metadata.setSolution("solution/mock");
    metadata.setReason(Map.of("mockReason", "someReason"));
    metadata.setCategories(Map.of("mockCategory", "someCategory"));
    metadata.setFeatures(Map.of("mockFeature", createMockFeatureMetadata()));

    recommendationMetadata.setMatchesMetadata(List.of(metadata));
    return recommendationMetadata;
  }

  private FeatureMetadataOut createMockFeatureMetadata() {
    var featureMetadata = new FeatureMetadataOut();
    featureMetadata.setSolution("solution/mock");
    featureMetadata.setAgentConfig("agentConfig/mock");
    featureMetadata.setReason(Map.of("mockReason", "someReason"));

    return featureMetadata;
  }
}
