package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import com.silenteight.solving.api.v1.Alert;
import com.silenteight.solving.api.v1.FeatureVectorSolution;
import com.silenteight.solving.api.v1.Match;
import com.silenteight.solving.api.v1.MatchFlag;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PendingAlertsTest {

  private final Fixtures fixtures = new Fixtures();

  @Test
  void shouldFindAlertExistingAlert() {
    var alerts = new PendingAlerts(List.of(fixtures.alert1));

    var alert = alerts.getById(1);

    assertThat(alert)
        .isNotEmpty()
        .map(PendingAlert::getAlertId)
        .hasValue(1L);
  }

  @Test
  void shouldReturnEmptyIfAlertIsMissing() {
    var alerts = new PendingAlerts(List.of(fixtures.alert1));

    var alert = alerts.getById(2);

    assertThat(alert).isEmpty();
  }

  @Test
  void shouldConvertToAlert() {
    var alerts = new PendingAlerts(List.of(fixtures.alert1));

    var request = alerts.toBatchSolveAlertsRequest("strategy");

    assertThat(request.getStrategy()).isEqualTo(fixtures.strategy);
    assertThat(request.getAlertsList()).containsExactly(fixtures.expectedAlert1);
  }

  @Test
  void shouldConvertedRequestHaveAlertsInTheSameOrder() {
    var alerts = new PendingAlerts(List.of(
        fixtures.alert1, fixtures.alert3, fixtures.alert2));

    var request = alerts.toBatchSolveAlertsRequest(fixtures.strategy);

    assertThat(request.getAlertsCount()).isEqualTo(3);
    assertThat(request.getAlertsList())
        .extracting(Alert::getName)
        .containsExactly("alerts/1", "alerts/3", "alerts/2");
  }

  private static class Fixtures {

    String strategy = "strategy";

    PendingAlert alert1 = PendingAlert.builder()
        .alertId(1)
        .matchSolutions(List.of(
            FeatureVectorSolution.SOLUTION_FALSE_POSITIVE,
            FeatureVectorSolution.SOLUTION_HINTED_FALSE_POSITIVE))
        .matchIds(new long[] { 1, 2 })
        .build();

    PendingAlert alert2 = PendingAlert.builder()
        .alertId(2)
        .matchSolutions(List.of(
            FeatureVectorSolution.SOLUTION_HINTED_FALSE_POSITIVE))
        .matchIds(new long[] { 3 })
        .build();

    PendingAlert alert3 = PendingAlert.builder()
        .alertId(3)
        .matchSolutions(List.of(
            FeatureVectorSolution.SOLUTION_FALSE_POSITIVE))
        .matchIds(new long[] { 4 })
        .build();

    Alert expectedAlert1 = Alert.newBuilder()
        .addMatches(Match.newBuilder()
            .setSolution(FeatureVectorSolution.SOLUTION_FALSE_POSITIVE)
            .addFlags(MatchFlag.MATCH_FLAG_UNSPECIFIED)
            .build())
        .addMatches(Match.newBuilder()
            .setSolution(FeatureVectorSolution.SOLUTION_HINTED_FALSE_POSITIVE)
            .addFlags(MatchFlag.MATCH_FLAG_UNSPECIFIED)
            .build())
        .setName("alerts/1")
        .build();
  }
}
