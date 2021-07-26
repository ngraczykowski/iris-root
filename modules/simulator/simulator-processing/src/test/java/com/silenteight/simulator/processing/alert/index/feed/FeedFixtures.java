package com.silenteight.simulator.processing.alert.index.feed;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;

import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class FeedFixtures {

  static final String REQUEST_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final String ANALYSIS_NAME = "analysis/01256804-1ce1-4d52-94d4-d1876910f272";
  static final String RECOMMENDATION_NAME = "recommendations/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  static final String ALERT_NAME = "alerts/de1afe98-0b58-4941-9791-4e081f9b8139";
  static final Timestamp RECOMMENDATION_CREATE_TIME = Timestamp.newBuilder()
      .setSeconds(1622505601)
      .setNanos(0)
      .build();
  static final String RECOMMENDED_ACTION = "FALSE_POSITIVE";
  static final String RECOMMENDATION_COMMENT = "This is not that person";
  static final String MATCH_NAME = "matches/a9b45451-6fde-4832-8dc0-d17b4708d8ca";
  static final String FEATURE_SOLUTION = "FALSE_POSITIVE";
  static final String MATCH_SOLUTION = "FALSE_POSITIVE";

  static final Recommendation RECOMMENDATION =
      Recommendation.newBuilder()
          .setName(RECOMMENDATION_NAME)
          .setAlert(ALERT_NAME)
          .setCreateTime(RECOMMENDATION_CREATE_TIME)
          .setRecommendedAction(RECOMMENDED_ACTION)
          .setRecommendationComment(RECOMMENDATION_COMMENT)
          .build();

  static final RecommendationInfo RECOMMENDATION_INFO =
      RecommendationInfo.newBuilder()
          .setAlert(ALERT_NAME)
          .setRecommendation(RECOMMENDATION_NAME)
          .build();

  static final RecommendationsGenerated REQUEST =
      RecommendationsGenerated.newBuilder()
          .setAnalysis(ANALYSIS_NAME)
          .addAllRecommendationInfos(singletonList(RECOMMENDATION_INFO))
          .build();

  private static final List<RecommendationInfo> RECOMMENDATIONS_LIST = IntStream
      .range(0, 10)
      .boxed()
      .map(v -> RECOMMENDATION_INFO)
      .collect(toList());

  static final RecommendationsGenerated MULTIPLE_ALERTS_REQUEST = RecommendationsGenerated
      .newBuilder()
      .setAnalysis(ANALYSIS_NAME)
      .addAllRecommendationInfos(RECOMMENDATIONS_LIST)
      .build();

  static final Struct FEATURE_REASON =
      Struct.newBuilder()
          .putAllFields(
              Map.of("reason-1", Value.newBuilder()
                  .setStringValue("This is reason")
                  .build()))
          .build();

  static final FeatureMetadata FEATURE_METADATA =
      FeatureMetadata.newBuilder()
          .setAgentConfig("agent-config")
          .setSolution(FEATURE_SOLUTION)
          .setReason(FEATURE_REASON)
          .build();

  static final MatchMetadata MATCH_METADATA =
      MatchMetadata.newBuilder()
          .setMatch(MATCH_NAME)
          .setSolution(MATCH_SOLUTION)
          .putAllCategories(Map.of("category-1", "category-value-1"))
          .putAllFeatures(Map.of("feature-name", FEATURE_METADATA))
          .build();

  static final RecommendationMetadata METADATA =
      RecommendationMetadata.newBuilder()
          .setName(RECOMMENDATION_NAME + "/metadata")
          .setAlert(ALERT_NAME)
          .addMatches(MATCH_METADATA)
          .build();
}
