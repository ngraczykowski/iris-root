package com.silenteight.warehouse.common.domain;

import com.silenteight.warehouse.common.properties.RecommendationProperties;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class RecommendationMapperTest {

  private static final String RECOMMENDATION_FIELD_NAME = "s8_recommendation";

  private final RecommendationMapper mapper = new RecommendationMapper(new RecommendationProperties(
      Map.of(
          "ACTION_FALSE_POSITIVE",
          List.of("ACTION_FALSE_POSITIVE", "ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE"),
          "ACTION_MANUAL_INVESTIGATION",
          List.of("ACTION_MANUAL_INVESTIGATION", "MANUAL_INVESTIGATION"),
          "ACTION_POTENTIAL_TRUE_POSITIVE", List.of("ACTION_POTENTIAL_TRUE_POSITIVE")),
      RECOMMENDATION_FIELD_NAME));


  @ParameterizedTest
  @CsvSource({
      "ACTION_FALSE_POSITIVE, ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE",
      "ACTION_FALSE_POSITIVE, ACTION_FALSE_POSITIVE",
      "ACTION_MANUAL_INVESTIGATION, ACTION_MANUAL_INVESTIGATION",
      "ACTION_MANUAL_INVESTIGATION, MANUAL_INVESTIGATION",
      "ACTION_POTENTIAL_TRUE_POSITIVE, ACTION_POTENTIAL_TRUE_POSITIVE" })
  void getRecommendationForDesiredPayloadRecommendationValue(
      Recommendation expectedValue, String payloadValue) {
    Recommendation recommendation = mapper.getRecommendationByValue(
        Map.of(RECOMMENDATION_FIELD_NAME, payloadValue));

    assertThat(recommendation).isEqualTo(expectedValue);
  }
}