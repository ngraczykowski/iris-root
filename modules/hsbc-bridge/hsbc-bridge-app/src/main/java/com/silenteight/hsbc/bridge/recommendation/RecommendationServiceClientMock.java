package com.silenteight.hsbc.bridge.recommendation;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.recommendation.metadata.FeatureMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.MatchMetadata;
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;

import org.springframework.retry.annotation.Retryable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class RecommendationServiceClientMock implements RecommendationServiceClient {

  private final RecommendationRepository repository;

  @Override
  @Retryable(value = CannotGetRecommendationsException.class)
  public List<RecommendationWithMetadataDto> getRecommendations(String analysis) {
    return getAlerts(analysis).stream().map(alert ->
        RecommendationWithMetadataDto.builder()
            .alert(alert)
            .name("recommendations/recommendation-" + UUID.randomUUID())
            .date(OffsetDateTime.now())
            .recommendedAction("MANUAL_INVESTIGATION")
            .recommendationComment("S8 Recommendation: Manual Investigation")
            .metadata(createMetadata())
            .build())
        .collect(Collectors.toList());
  }

  private List<String> getAlerts(String analysis) {
    return repository.getAlertsByAnalysis(analysis);
  }

  private RecommendationMetadata createMetadata() {
    var metadata = new RecommendationMetadata();
    metadata.setMatchesMetadata(List.of(createMatchMetadata()));
    return metadata;
  }

  private MatchMetadata createMatchMetadata() {
    var metadata = new MatchMetadata();
    metadata.setFeatures(createFeatures());
    metadata.setMatch("alerts/1/matches/1");
    metadata.setSolution("SOLUTION_FALSE_POSITIVE");
    metadata.setReason(Map.of(
        "feature_vector_signature", "J4VGkp1+FaNsaGDtBXgQsWpUYDo=",
        "policy", "policies/5afc2f12-85c0-4fb3-992e-1552ac843ceb",
        "step", "steps/e6ceb774-ab56-4576-b653-1cdceb2d25e7"
    ));
    metadata.setCategories(Map.of(
        "category_1", "category_1_value",
        "category_2", "category_2_value",
        "category_3", "category_3_value"
    ));
    return metadata;
  }

  private Map<String, FeatureMetadata> createFeatures() {
    return Map.of(
        "features/name", createFeatureMetadata()
    );
  }

  private FeatureMetadata createFeatureMetadata() {
    var metadata = new FeatureMetadata();
    metadata.setAgentConfig("agents/name/versions/1.0.0/configs/1");
    metadata.setSolution("EXACT_MATCH");
    metadata.setReason(Map.of(
        "name", "Some dummy reason"
    ));
    return metadata;
  }
}
