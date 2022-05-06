package com.silenteight.warehouse.statistics.computers;

import com.silenteight.warehouse.common.domain.AnalystDecisionMapper;
import com.silenteight.warehouse.common.domain.RecommendationMapper;
import com.silenteight.warehouse.common.properties.AnalystDecisionProperties;
import com.silenteight.warehouse.common.properties.RecommendationProperties;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AlertRecommendationComputerTest {

  private static final String RECOMMENDATION_FIELD_NAME =
      "s8_recommendation";
  private static final String ANALYST_DECISION_FIELD_NAME = "analyst_decision";
  
  private final AlertRecommendationComputer computer =
      new AlertRecommendationComputer(
          new RecommendationMapper(
              new RecommendationProperties(
                  Map.of(
                      "ACTION_FALSE_POSITIVE", List.of("ACTION_FALSE_POSITIVE"),
                      "ACTION_MANUAL_INVESTIGATION", List.of("ACTION_MANUAL_INVESTIGATION"),
                      "ACTION_POTENTIAL_TRUE_POSITIVE", List.of("ACTION_POTENTIAL_TRUE_POSITIVE")),
                  RECOMMENDATION_FIELD_NAME)),
          new AnalystDecisionMapper(
              new AnalystDecisionProperties(
                  Map.of(
                      "FALSE_POSITIVE", List.of("analyst_decision_false_positive"),
                      "TRUE_POSITIVE", List.of("analyst_decision_true_positive")),
                  ANALYST_DECISION_FIELD_NAME)));

  private static AlertDto buildAlert(
      String recommendation, String analystDecision) {
    return buildAlert(
        Map.of(RECOMMENDATION_FIELD_NAME, recommendation, ANALYST_DECISION_FIELD_NAME,
            analystDecision));
  }

  private static AlertDto buildAlert(
      String recommendation) {
    return buildAlert(Map.of(RECOMMENDATION_FIELD_NAME, recommendation));
  }

  private static AlertDto buildAlert(Map<String, String> payload) {
    return AlertDto
        .builder()
        .id(1L)
        .name("alertName")
        .discriminator("test_desc")
        .createdAt(Timestamp.from(Instant.now()))
        .recommendationDate(Timestamp.from(Instant.now()))
        .payload(payload)
        .build();
  }

  @Test
  void calculateProperlyWhenAllDataAsSet() {
    // Given
    List<AlertDto> alerts = List.of(
        buildAlert("ACTION_FALSE_POSITIVE"),
        buildAlert("ACTION_FALSE_POSITIVE", "analyst_decision_false_positive"),
        buildAlert("ACTION_MANUAL_INVESTIGATION", "analyst_decision_false_positive"),
        buildAlert("ACTION_POTENTIAL_TRUE_POSITIVE")
    );

    // When
    AlertsRecommendationStatistics statistics = computer.compute(alerts);

    // Then
    assertThat(statistics).isEqualTo(AlertsRecommendationStatistics
        .builder()
        .alertsCount(4)
        .effectivenessPercent(100.0)
        .analyticsDecisionCount(1)
        .efficiencyPercent(75.0)
        .manualInvestigationsCount(1)
        .falsePositivesCount(2)
        .potentialTruePositivesCount(1)
        .build());
  }

  @Test
  void calculateProperlyWhenAllDataAsSet2() {
    // Given
    List<AlertDto> alerts = List.of(
        buildAlert("ACTION_FALSE_POSITIVE"),
        buildAlert("ACTION_FALSE_POSITIVE", "analyst_decision_false_positive"),
        buildAlert("ACTION_MANUAL_INVESTIGATION", "analyst_decision_false_positive"),
        buildAlert("ACTION_POTENTIAL_TRUE_POSITIVE", "analyst_decision_false_positive")
    );

    // When
    AlertsRecommendationStatistics statistics = computer.compute(alerts);

    // Then
    assertThat(statistics).isEqualTo(AlertsRecommendationStatistics
        .builder()
        .alertsCount(4)
        .effectivenessPercent(50.0)
        .analyticsDecisionCount(2)
        .efficiencyPercent(75.0)
        .manualInvestigationsCount(1)
        .falsePositivesCount(2)
        .potentialTruePositivesCount(1)
        .build());
  }

  @Test
  void omitAlertWhenRecommendationIsUnspecified() {
    // Given
    List<AlertDto> alerts = List.of(
        buildAlert("random")
    );

    // When
    AlertsRecommendationStatistics statistics = computer.compute(alerts);

    // Then
    assertThat(statistics).isEqualTo(AlertsRecommendationStatistics
        .builder()
        .alertsCount(1)
        .effectivenessPercent(Double.NaN)
        .analyticsDecisionCount(0)
        .efficiencyPercent(0.0)
        .manualInvestigationsCount(0)
        .falsePositivesCount(0)
        .potentialTruePositivesCount(0)
        .build());
  }
}