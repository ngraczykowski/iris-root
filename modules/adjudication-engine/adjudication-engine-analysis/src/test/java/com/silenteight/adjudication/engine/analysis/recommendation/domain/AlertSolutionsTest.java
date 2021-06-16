package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AlertSolutionsTest {

  private PendingAlert pendingAlert;

  @BeforeEach
  void setUp() {
    pendingAlert = new PendingAlert(1, List.of(
        FeatureVectorSolution.SOLUTION_FALSE_POSITIVE,
        FeatureVectorSolution.SOLUTION_HINTED_FALSE_POSITIVE));
  }

  @Test
  void shouldConvertToAlert() {
    assertThat(pendingAlert.toAlert().getMatchesList().size()).isEqualTo(2);
  }
}
