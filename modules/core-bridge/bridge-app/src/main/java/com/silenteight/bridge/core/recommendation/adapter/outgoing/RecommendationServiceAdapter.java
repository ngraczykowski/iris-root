package com.silenteight.bridge.core.recommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.library.v1.AdjudicationEngineLibraryRuntimeException;
import com.silenteight.adjudication.api.library.v1.recommendation.*;
import com.silenteight.bridge.core.recommendation.domain.model.FeatureMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;
import com.silenteight.bridge.core.recommendation.domain.port.outgoing.RecommendationService;

import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class RecommendationServiceAdapter implements RecommendationService {

  private final RecommendationServiceClient recommendationServiceClient;

  @Override
  @Retryable(AdjudicationEngineLibraryRuntimeException.class)
  public List<RecommendationWithMetadata> getRecommendations(String analysisName) {
    return recommendationServiceClient.getRecommendations(analysisName).stream()
        .map(e -> mapToRecommendationWithMetadata(e, analysisName))
        .toList();
  }

  private RecommendationWithMetadata mapToRecommendationWithMetadata(
      RecommendationWithMetadataOut recommendation, String analysisName) {
    return RecommendationWithMetadata.builder()
        .name(recommendation.getName())
        .alertName(recommendation.getAlert())
        .analysisName(analysisName)
        .recommendedAt(recommendation.getDate())
        .recommendedAction(recommendation.getRecommendedAction())
        .recommendationComment(recommendation.getRecommendationComment())
        .metadata(mapToRecommendationMetadata(recommendation.getMetadata()))
        .build();
  }

  private RecommendationMetadata mapToRecommendationMetadata(RecommendationMetadataOut metadata) {
    var matchMetadata = metadata.getMatchesMetadata().stream()
        .map(this::mapToMatchMetadata)
        .toList();

    return new RecommendationMetadata(matchMetadata);
  }

  private MatchMetadata mapToMatchMetadata(MatchMetadataOut metadata) {
    return MatchMetadata.builder()
        .match(metadata.getMatch())
        .solution(metadata.getSolution())
        .reason(metadata.getReason())
        .categories(metadata.getCategories())
        .features(mapToFeatureMetadataMap(metadata.getFeatures()))
        .build();
  }

  private Map<String, FeatureMetadata> mapToFeatureMetadataMap(
      Map<String, FeatureMetadataOut> features) {

    return features.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> this.mapToFeatureMetadata(e.getValue())));
  }

  private FeatureMetadata mapToFeatureMetadata(FeatureMetadataOut featureMetadataOut) {
    return FeatureMetadata.builder()
        .agentConfig(featureMetadataOut.getAgentConfig())
        .reason(featureMetadataOut.getReason())
        .solution(featureMetadataOut.getSolution())
        .build();
  }
}
