package com.silenteight.simulator.processing.alert.index.fixtures;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;

import com.google.protobuf.Struct;
import com.google.protobuf.Timestamp;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RecommendationFixtures {

  public static final String REQUEST_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  public static final String ANALYSIS_NAME = "analysis/1";
  public static final String POLICY_NAME = "policies/8803c2cb-48df-4d7e-8be6-043a3c56ade8";
  public static final String POLICY_TITLE = "Name agent policy";
  public static final String FEATURE_VECTOR_SIGNATURE = "ByG4Am28zh+Qj6/JCPz6Q+XnOH0=";
  public static final String RECOMMENDATION_NAME =
      "recommendations/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  public static final String RECOMMENDATION_METADATA_NAME = RECOMMENDATION_NAME + "/metadata";
  public static final String ALERT_NAME = "alerts/de1afe98-0b58-4941-9791-4e081f9b8139";
  public static final Timestamp RECOMMENDATION_CREATE_TIME = Timestamp.newBuilder()
      .setSeconds(1622505601)
      .setNanos(0)
      .build();

  public static final String RECOMMENDED_ACTION = "FALSE_POSITIVE";
  public static final String RECOMMENDATION_COMMENT = "This is not that person";
  public static final String MATCH_NAME = "matches/a9b45451-6fde-4832-8dc0-d17b4708d8ca";
  public static final String FEATURE_SOLUTION = "FALSE_POSITIVE";
  public static final String MATCH_SOLUTION_FP = "FALSE_POSITIVE";
  public static final String MATCH_SOLUTION_TP = "TRUE_POSITIVE";
  public static final String AGENTS_CONFIG = "agents/name/versions/1.0.0/configs/1";
  public static final String FEATURES_NAMES_KEYS = "features/name";
  public static final Recommendation RECOMMENDATION =
      Recommendation.newBuilder()
          .setName(RECOMMENDATION_NAME)
          .setAlert(ALERT_NAME)
          .setCreateTime(RECOMMENDATION_CREATE_TIME)
          .setRecommendedAction(RECOMMENDED_ACTION)
          .setRecommendationComment(RECOMMENDATION_COMMENT)
          .build();

  public static final RecommendationInfo RECOMMENDATION_INFO =
      RecommendationInfo.newBuilder()
          .setAlert(ALERT_NAME)
          .setRecommendation(RECOMMENDATION_NAME)
          .build();

  public static final RecommendationsGenerated REQUEST =
      RecommendationsGenerated.newBuilder()
          .setAnalysis(ANALYSIS_NAME)
          .addAllRecommendationInfos(singletonList(RECOMMENDATION_INFO))
          .build();

  private static final List<RecommendationInfo> RECOMMENDATIONS_LIST = IntStream
      .range(0, 10)
      .boxed()
      .map(v -> RECOMMENDATION_INFO)
      .collect(toList());

  public static final RecommendationsGenerated MULTIPLE_ALERTS_REQUEST = RecommendationsGenerated
      .newBuilder()
      .setAnalysis(ANALYSIS_NAME)
      .addAllRecommendationInfos(RECOMMENDATIONS_LIST)
      .build();

  public static final Struct FEATURE_REASON =
      Struct.newBuilder()
          .putAllFields(
              Map.of("reason-1", getStringAsValue("This is reason")))
          .build();

  public static final Struct MATCH_REASON =
      Struct.newBuilder()
          .putAllFields(
              Map.of("step", getStringAsValue(""),
                  "policy", getStringAsValue(POLICY_NAME),
                  "step_title", getStringAsValue(" "),
                  "policy_title", getStringAsValue(POLICY_TITLE),
                  "feature_vector_signature", getStringAsValue(FEATURE_VECTOR_SIGNATURE)))
          .build();

  public static final FeatureMetadata FEATURE_METADATA =
      FeatureMetadata.newBuilder()
          .setAgentConfig("agent-config")
          .setSolution("FEATURE_METADATA_SOLUTION")
          .setReason(FEATURE_REASON)
          .build();

  public static final MatchMetadata MATCH_METADATA_1 =
      MatchMetadata.newBuilder()
          .setMatch(MATCH_NAME)
          .setSolution(MATCH_SOLUTION_FP)
          .putAllCategories(Map.of("category-1", "category-value-1"))
          .putAllFeatures(Map.of(FEATURES_NAMES_KEYS, FEATURE_METADATA))
          .build();

  public static final RecommendationMetadata METADATA =
      RecommendationMetadata.newBuilder()
          .setName(RECOMMENDATION_METADATA_NAME)
          .setAlert(ALERT_NAME)
          .addMatches(MATCH_METADATA_1)
          .build();

  public static final RecommendationWithMetadata RECOMMENDATION_WITH_METADATA =
      RecommendationWithMetadata.newBuilder()
          .setRecommendation(RECOMMENDATION)
          .setMetadata(METADATA)
          .build();

  public static final List<RecommendationWithMetadata> RECOMMENDATION_WITH_METADATA_LIST
      = IntStream
      .range(0, 10)
      .boxed()
      .map(v -> RECOMMENDATION_WITH_METADATA)
      .collect(toList());

  public static final FeatureMetadata FEATURE_METADATA_2 = FeatureMetadata.newBuilder()
      .setAgentConfig(AGENTS_CONFIG)
      .setSolution(FEATURE_SOLUTION)
      .setReason(FEATURE_REASON)
      .build();

  public static final MatchMetadata MATCH_METADATA_2 = MatchMetadata.newBuilder()
      .setSolution(MATCH_SOLUTION_TP)
      .putFeatures(FEATURES_NAMES_KEYS, FEATURE_METADATA_2)
      .setReason(MATCH_REASON)
      .build();

  public static final RecommendationMetadata RECOMMENDATION_METADATA_WITH_MULTI_HIT_ALERT =
      RecommendationMetadata.newBuilder()
          .setAlert(ALERT_NAME)
          .addAllMatches(List.of(MATCH_METADATA_1, MATCH_METADATA_2))
          .build();

  public static final RecommendationInfo RECOMMENDATION_INFO_WITH_MULTI_HIT_ALERT =
      RecommendationInfo.newBuilder()
          .setAlert(ALERT_NAME)
          .setValue(RECOMMENDATION)
          .setMetadata(RECOMMENDATION_METADATA_WITH_MULTI_HIT_ALERT)
          .build();

  public static final RecommendationsGenerated REQUEST_WITH_MULTI_HIT_ALERT =
      RecommendationsGenerated.newBuilder()
          .setAnalysis(ANALYSIS_NAME)
          .addAllRecommendationInfos(List.of(RECOMMENDATION_INFO_WITH_MULTI_HIT_ALERT))
          .build();

  public static Value getStringAsValue(String string) {
    return Value.newBuilder()
        .setStringValue(string)
        .build();
  }
}
