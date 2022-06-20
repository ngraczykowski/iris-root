/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlert.RecommendationEnum;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Alert;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction.*;
import static org.assertj.core.api.Assertions.*;

class GnsRtResponseMapperTest {

  private final Fixtures fixtures = new Fixtures();

  private GnsRtResponseMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new GnsRtResponseMapper(properties());
  }

  private GnsRtResponseMapperConfigurationProperties properties() {
    return new GnsRtResponseMapperConfigurationProperties(true);
  }

  @Test
  void simpleFieldsMappingTest() {
    var result = map(fixtures.alert, fixtures.falsePositive);

    Assertions.assertThat(result)
        .satisfies(r -> Assertions.assertThat(r.getAlertId()).isEqualTo(fixtures.id))
        .satisfies(r -> Assertions.assertThat(r.getWatchlistType()).isEqualTo(fixtures.watchlistType))
        .satisfies(r -> Assertions.assertThat(r.getRecommendationTimestamp()).isEqualTo(fixtures.createdAt));
  }

  private GnsRtResponseAlert map(GnsRtAlert alert, Recommendation alertRecommendation) {
    return mapper.map(alert, alertRecommendation);
  }

  @Test
  void recommendedActionMappingTest() {
    assertMappedRecommendation(fixtures.falsePositive).isEqualTo(RecommendationEnum.FALSE_POSITIVE);
    assertMappedRecommendation(fixtures.potentialTruePositive).isEqualTo(
        RecommendationEnum.POTENTIAL_TRUE_POSITIVE);
    assertMappedRecommendation(fixtures.investigate).isEqualTo(RecommendationEnum.INVESTIGATE);
    assertMappedRecommendation(fixtures.hintedFalsePositive).isEqualTo(
        RecommendationEnum.INVESTIGATE);
    assertMappedRecommendation(fixtures.hintedTruePositive).isEqualTo(RecommendationEnum.INVESTIGATE);
    assertMappedRecommendation(fixtures.partiallyFalsePositive).isEqualTo(
        RecommendationEnum.INVESTIGATE);
  }

  private AbstractObjectAssert<?, RecommendationEnum> assertMappedRecommendation(
      Recommendation recommendation) {

    var response = map(fixtures.alert, recommendation);

    return assertThat(response).extracting(GnsRtResponseAlert::getRecommendation);
  }

  private static class Fixtures {

    String id = "id";
    String comment = "comment";
    String createdAt = "2019-01-22T09:21:28";
    String watchlistType = "AM";

    GnsRtAlert alert = alert();

    Recommendation falsePositive = recommendation(ACTION_FALSE_POSITIVE);
    Recommendation investigate = recommendation(ACTION_INVESTIGATE);
    Recommendation hintedFalsePositive = recommendation(ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE);
    Recommendation hintedTruePositive = recommendation(ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE);
    Recommendation partiallyFalsePositive =
        recommendation(ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE);
    Recommendation potentialTruePositive = recommendation(ACTION_POTENTIAL_TRUE_POSITIVE);

    private GnsRtAlert alert() {
      var alert = new GnsRtAlert();
      alert.setAlertId(id);
      alert.setWatchlistType(watchlistType);
      return alert;
    }

    private Recommendation recommendation(RecommendedAction action) {
      return Recommendation.builder()
          .matches(List.of())
          .alert(Alert.builder()
              .id(id)
              .build())
          .recommendedAction(action)
          .recommendedComment(comment)
          .recommendedAt(
              OffsetDateTime.ofInstant(Instant.parse(createdAt + "Z"), ZoneId.systemDefault()))
          .build();
    }

  }
}
