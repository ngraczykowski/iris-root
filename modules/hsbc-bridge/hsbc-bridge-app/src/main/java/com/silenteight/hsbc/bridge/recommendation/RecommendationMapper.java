package com.silenteight.hsbc.bridge.recommendation;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
class RecommendationMapper {

  private final Map<HsbcRecommendation, String> userFriendlyValues;

  RecommendationMapper(
      @NonNull Map<S8Recommendation, String> s8Values,
      @NonNull Map<HsbcRecommendation, String> userFriendlyValues) {
    this.userFriendlyValues = userFriendlyValues;

    this.falsePositive = s8Values.getOrDefault(S8Recommendation.FALSE_POSITIVE, "ACTION_FALSE_POSITIVE");
    this.manualInvestigation = s8Values.getOrDefault(S8Recommendation.MANUAL_INVESTIGATION, "ACTION_INVESTIGATE");
    this.potentialTruePositive =
        s8Values.getOrDefault(S8Recommendation.POTENTIAL_TRUE_POSITIVE, "ACTION_POTENTIAL_TRUE_POSITIVE");
  }

  private final String falsePositive;
  private final String manualInvestigation;
  private final String potentialTruePositive;

  String getRecommendationValue(String recommendedAction, String extendedAttribute5) {
    var recommendation = toRecommendation(recommendedAction, extendedAttribute5);

    return getUserFriendlyValue(recommendation);
  }

  private String getUserFriendlyValue(HsbcRecommendation recommendation) {
    return Optional.ofNullable(userFriendlyValues.get(recommendation)).orElseGet(() -> {
      log.warn("User-friendly value not configured for RecommendedEnum: {}", recommendation);
      return recommendation.name();
    });
  }

  private HsbcRecommendation toRecommendation(String recommendedAction, String extendedAttribute5) {
    if (falsePositive.equals(recommendedAction)) {
      return HsbcRecommendation.AAA_FALSE_POSITIVE;
    } else if (isManualInvestigation(recommendedAction)) {
      return isSanOrSsc(extendedAttribute5) ? HsbcRecommendation.LEVEL_2_REVIEW : HsbcRecommendation.LEVEL_1_REVIEW;
    } else if (potentialTruePositive.equals(recommendedAction) && !isSanOrSsc(extendedAttribute5)) {
      return HsbcRecommendation.LEVEL_2_REVIEW;
    }

    return HsbcRecommendation.LEVEL_3_REVIEW;
  }

  private boolean isManualInvestigation(String recommendedAction) {
    return Objects.isNull(recommendedAction) || recommendedAction.startsWith(manualInvestigation);
  }

  private boolean isSanOrSsc(String extendedAttribute5) {
    return "SAN".equalsIgnoreCase(extendedAttribute5) || "SSC".equalsIgnoreCase(extendedAttribute5);
  }
}
