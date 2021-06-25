package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.silenteight.hsbc.bridge.recommendation.HsbcRecommendation.LEVEL_1_REVIEW;
import static com.silenteight.hsbc.bridge.recommendation.HsbcRecommendation.LEVEL_2_REVIEW;
import static com.silenteight.hsbc.bridge.recommendation.HsbcRecommendation.LEVEL_3_REVIEW;
import static com.silenteight.hsbc.bridge.recommendation.S8Recommendation.FALSE_POSITIVE;
import static com.silenteight.hsbc.bridge.recommendation.S8Recommendation.MANUAL_INVESTIGATION;
import static com.silenteight.hsbc.bridge.recommendation.S8Recommendation.POTENTIAL_TRUE_POSITIVE;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Slf4j
class RecommendationMapper {

  private final Map<HsbcRecommendation, String> userFriendlyValues;

  RecommendationMapper(
      @NonNull Map<S8Recommendation, String> s8Values,
      @NonNull Map<HsbcRecommendation, String> userFriendlyValues) {
    this.userFriendlyValues = userFriendlyValues;

    this.falsePositive = s8Values.getOrDefault(FALSE_POSITIVE, "ACTION_FALSE_POSITIVE");
    this.manualInvestigation = s8Values.getOrDefault(MANUAL_INVESTIGATION, "ACTION_INVESTIGATE");
    this.potentialTruePositive =
        s8Values.getOrDefault(POTENTIAL_TRUE_POSITIVE, "ACTION_POTENTIAL_TRUE_POSITIVE");
  }

  private final String falsePositive;
  private final String manualInvestigation;
  private final String potentialTruePositive;

  String getRecommendationValue(String recommendedAction, String extendedAttribute3) {
    var recommendation = toRecommendation(recommendedAction, extendedAttribute3);

    return getUserFriendlyValue(recommendation);
  }

  private String getUserFriendlyValue(HsbcRecommendation recommendation) {
    return ofNullable(userFriendlyValues.get(recommendation)).orElseGet(() -> {
      log.warn("User-friendly value not configured for RecommendedEnum: {}", recommendation);
      return recommendation.name();
    });
  }

  private HsbcRecommendation toRecommendation(String recommendedAction, String extendedAttribute5) {
    if (falsePositive.equals(recommendedAction)) {
      return HsbcRecommendation.AAA_FALSE_POSITIVE;
    } else if (isManualInvestigation(recommendedAction)) {
      return isSanOrSsc(extendedAttribute5) ? LEVEL_2_REVIEW : LEVEL_1_REVIEW;
    } else if (potentialTruePositive.equals(recommendedAction) && !isSanOrSsc(extendedAttribute5)) {
      return LEVEL_2_REVIEW;
    }

    return LEVEL_3_REVIEW;
  }

  private boolean isManualInvestigation(String recommendedAction) {
    return isNull(recommendedAction) || recommendedAction.startsWith(manualInvestigation);
  }

  private boolean isSanOrSsc(String extendedAttribute5) {
    return "SAN".equalsIgnoreCase(extendedAttribute5) || "SSC".equalsIgnoreCase(extendedAttribute5);
  }
}
