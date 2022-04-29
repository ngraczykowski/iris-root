package com.silenteight.warehouse.statistics.computers;

import com.silenteight.warehouse.common.domain.Recommendation;
import com.silenteight.warehouse.common.domain.RecommendationMapper;
import com.silenteight.warehouse.common.properties.RecommendationProperties;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AlertRecommendationComputerTest {

  private static final String RECOMMENDATION_FIELD_NAME = "s8_recommendation";
  private final AlertRecommendationComputer computer =
      new AlertRecommendationComputer(new RecommendationMapper(new RecommendationProperties(
          Map.of(
              "ACTION_FALSE_POSITIVE", List.of("ACTION_FALSE_POSITIVE"),
              "ACTION_MANUAL_INVESTIGATION", List.of("ACTION_MANUAL_INVESTIGATION"),
              "ACTION_POTENTIAL_TRUE_POSITIVE", List.of("ACTION_POTENTIAL_TRUE_POSITIVE")),
          RECOMMENDATION_FIELD_NAME)));

  private static AlertDto buildAlert(Recommendation recommendation) {
    return AlertDto
        .builder()
        .id(1L)
        .name("alertName")
        .discriminator("test_desc")
        .createdAt(Timestamp.from(Instant.now()))
        .recommendationDate(Timestamp.from(Instant.now()))
        .payload(Map.of(RECOMMENDATION_FIELD_NAME, recommendation.name()))
        .build();
  }

  @Test
  void calculateProperlyWhenAllDataAsSet() {
    // Given
    List<AlertDto> alerts = List.of(
        buildAlert(Recommendation.ACTION_FALSE_POSITIVE),
        buildAlert(Recommendation.ACTION_FALSE_POSITIVE),
        buildAlert(Recommendation.ACTION_MANUAL_INVESTIGATION),
        buildAlert(Recommendation.ACTION_POTENTIAL_TRUE_POSITIVE)
    );

    // When
    AlertsRecommendationStatistics statistics = computer.compute(alerts);

    // Then
    assertThat(statistics).isEqualTo(AlertsRecommendationStatistics
        .builder()
        .alertsCount(4)
        .effectivenessPercent(null)
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
        buildAlert(Recommendation.UNSPECIFIED)
    );

    // When
    AlertsRecommendationStatistics statistics = computer.compute(alerts);

    // Then
    assertThat(statistics).isEqualTo(AlertsRecommendationStatistics
        .builder()
        .alertsCount(1)
        .effectivenessPercent(null)
        .efficiencyPercent(0.0)
        .manualInvestigationsCount(0)
        .falsePositivesCount(0)
        .potentialTruePositivesCount(0)
        .build());
  }
}