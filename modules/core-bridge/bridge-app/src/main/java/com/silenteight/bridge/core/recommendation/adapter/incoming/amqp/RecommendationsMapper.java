package com.silenteight.bridge.core.recommendation.adapter.incoming.amqp;

import com.silenteight.adjudication.api.library.v1.util.StructMapperUtil;
import com.silenteight.adjudication.api.library.v1.util.TimeStampUtil;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.bridge.core.recommendation.domain.model.FeatureMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.MatchMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationMetadata;
import com.silenteight.bridge.core.recommendation.domain.model.RecommendationWithMetadata;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
class RecommendationsMapper {

  private static final boolean RECOMMENDATION_NOT_TIMED_OUT = false;

  RecommendationWithMetadata toRecommendationWithMetadata(
      RecommendationInfo recommendationInfo, String analysisName) {
    var recommendation = recommendationInfo.getValue();

    return RecommendationWithMetadata.builder()
        .name(recommendation.getName())
        .alertName(recommendation.getAlert())
        .analysisName(analysisName)
        .recommendedAt(TimeStampUtil.toOffsetDateTime(recommendation.getCreateTimeOrBuilder()))
        .recommendedAction(recommendation.getRecommendedAction())
        .recommendationComment(recommendation.getRecommendationComment())
        .metadata(toRecommendationMetadata(recommendationInfo.getMetadata()))
        .timeout(RECOMMENDATION_NOT_TIMED_OUT)
        .build();
  }

  private RecommendationMetadata toRecommendationMetadata(
      com.silenteight.adjudication.api.v2.RecommendationMetadata metadata) {
    var matchMetadata = metadata.getMatchesList().stream()
        .map(this::toMatchMetadata)
        .toList();

    return new RecommendationMetadata(matchMetadata);
  }

  private MatchMetadata toMatchMetadata(
      com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata metadata) {
    return MatchMetadata.builder()
        .match(metadata.getMatch())
        .solution(metadata.getSolution())
        .reason(StructMapperUtil.toMap(metadata.getReason()))
        .categories(metadata.getCategoriesMap())
        .features(toFeatureMetadataMap(metadata.getFeaturesMap()))
        .matchComment(metadata.getMatchComment())
        .build();
  }

  private Map<String, FeatureMetadata> toFeatureMetadataMap(
      Map<String, com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata>
          featuresMap) {
    return featuresMap.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> toFeatureMetadata(e.getValue())));
  }

  private FeatureMetadata toFeatureMetadata(
      com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata metadata) {
    return FeatureMetadata.builder()
        .agentConfig(metadata.getAgentConfig())
        .reason(StructMapperUtil.toMap(metadata.getReason()))
        .solution(metadata.getSolution())
        .build();
  }
}
