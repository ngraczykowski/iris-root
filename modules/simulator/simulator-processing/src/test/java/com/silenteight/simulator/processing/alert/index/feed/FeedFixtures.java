package com.silenteight.simulator.processing.alert.index.feed;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated;
import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;

import java.util.Map;

import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class FeedFixtures {

  static final String REQUEST_ID = "b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  static final String ANALYSIS_NAME = "analysis/01256804-1ce1-4d52-94d4-d1876910f272";
  static final String ALERT_NAME = "alerts/de1afe98-0b58-4941-9791-4e081f9b8139";
  static final String RECOMMENDATION_NAME = "recommendations/d17b4708-6fde-8dc0-4832-d17b4708d8ca";
  static final Map<String, String> ALERT_LABELS = Map.of("country", "SG", "riskType", "PEP");

  static final RecommendationInfo RECOMMENDATION =
      RecommendationInfo.newBuilder()
          .setAlert(ALERT_NAME)
          .setRecommendation(RECOMMENDATION_NAME)
          .build();

  static final Alert ALERT =
      Alert.newBuilder()
          .setName(ALERT_NAME)
          .putAllLabels(ALERT_LABELS)
          .build();

  static final RecommendationsGenerated REQUEST =
      RecommendationsGenerated.newBuilder()
          .setAnalysis(ANALYSIS_NAME)
          .addAllRecommendationInfos(singletonList(RECOMMENDATION))
          .build();
}
