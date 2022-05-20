package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.silenteight.adjudication.engine.analysis.recommendation.RecommendationFixture.createAlertRecommendation;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AlertRecommendationsTest {

  private AlertRecommendation alertRecommendation;

  @BeforeEach
  void setUp() {
    alertRecommendation = createAlertRecommendation();
  }

  @Test
  void shouldConvertToMetaDate() {
    var alertMetaData = alertRecommendation.toMetadata();
    assertThat(alertMetaData.getMatchesCount()).isEqualTo(4);
    assertThat(alertMetaData.getAlert()).isEqualTo("alerts/1");
    assertThat(alertMetaData.getName()).isEqualTo("analysis/1/recommendations/1/metadata");
  }
}
