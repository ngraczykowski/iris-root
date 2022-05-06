package com.silenteight.warehouse.statistics.aggregators;

import com.silenteight.warehouse.common.domain.AnalystDecisionMapper;
import com.silenteight.warehouse.common.domain.RecommendationMapper;
import com.silenteight.warehouse.common.properties.AnalystDecisionProperties;
import com.silenteight.warehouse.common.properties.RecommendationProperties;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;
import com.silenteight.warehouse.statistics.computers.AlertRecommendationComputer;
import com.silenteight.warehouse.statistics.computers.AlertsRecommendationStatistics;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class AlertRecommendationCollectorTest {

  private static final String RECOMMENDATION_FIELD_NAME = "s8_recommendation";
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

  private static AlertDto alertDtoBuilderAsPT() {
    return alertDtoBuilder(Map.of("s8_recommendation", "ACTION_POTENTIAL_TRUE_POSITIVE"));
  }

  private static AlertDto alertDtoBuilderAsFP() {
    return alertDtoBuilder(Map.of("s8_recommendation", "ACTION_FALSE_POSITIVE"));
  }

  private static AlertDto alertDtoBuilderAsMI() {
    return alertDtoBuilder(Map.of("s8_recommendation", "ACTION_MANUAL_INVESTIGATION"));
  }

  private static AlertDto alertDtoBuilder(Map<String, String> map) {
    var now = Timestamp.from(Instant.now());
    return AlertDto
        .builder()
        .id(1L)
        .name("alertName")
        .discriminator("test_desc")
        .createdAt(now)
        .recommendationDate(now)
        .payload(map)
        .build();
  }

  @Test
  void listOfAlerts_calculationAreFine() {
    // Given
    List<AlertDto> alerts = List.of(
        alertDtoBuilderAsPT(),
        alertDtoBuilderAsPT(),
        alertDtoBuilderAsFP(),
        alertDtoBuilderAsFP(),
        alertDtoBuilderAsFP(),
        alertDtoBuilderAsMI());

    // When
    AlertsRecommendationStatistics alertsRecommendationStatistics = computer.compute(alerts);

    // Then
    assertThat(alertsRecommendationStatistics).isEqualTo(
        AlertsRecommendationStatistics.builder().alertsCount(6).falsePositivesCount(3)
            .manualInvestigationsCount(1)
            .potentialTruePositivesCount(2)
            .effectivenessPercent(Double.NaN)
            .efficiencyPercent(((double) 5 / 6) * 100)
            .build());
  }

  @Test
  void noAlerts_effectivenessAndEfficiencyAsNull() {
    // Given
    List<AlertDto> alerts = List.of();

    // When
    AlertsRecommendationStatistics alertsRecommendationStatistics = computer.compute(alerts);

    // Then
    assertThat(alertsRecommendationStatistics).isEqualTo(
        AlertsRecommendationStatistics.builder().alertsCount(0).falsePositivesCount(0)
            .manualInvestigationsCount(0)
            .potentialTruePositivesCount(0)
            .effectivenessPercent(Double.NaN)
            .efficiencyPercent(Double.NaN)
            .build());
  }

  @Test
  void onlyManualInvestigationAlerts_effectivenessNullAndEfficiencyAs0() {
    // Given
    List<AlertDto> alerts = List.of(
        alertDtoBuilderAsMI());

    // When
    AlertsRecommendationStatistics alertsRecommendationStatistics = computer.compute(alerts);

    // Then
    assertThat(alertsRecommendationStatistics).isEqualTo(
        AlertsRecommendationStatistics.builder().alertsCount(1).falsePositivesCount(0)
            .manualInvestigationsCount(1)
            .potentialTruePositivesCount(0)
            .effectivenessPercent(Double.NaN)
            .efficiencyPercent(0.0)
            .build());
  }
}