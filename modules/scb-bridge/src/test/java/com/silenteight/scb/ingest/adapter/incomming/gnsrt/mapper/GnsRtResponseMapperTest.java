package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.proto.serp.v1.recommendation.RecommendedAction;
import com.silenteight.protocol.utils.MoreTimestamps;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtAlert;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlerts;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlerts.RecommendationEnum;

import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.silenteight.proto.serp.v1.recommendation.RecommendedAction.*;
import static com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlerts.RecommendationEnum.FALSE_POSITIVE;
import static com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlerts.RecommendationEnum.INVESTIGATE;
import static com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtResponseAlerts.RecommendationEnum.POTENTIAL_TRUE_POSITIVE;
import static org.assertj.core.api.Assertions.*;

class GnsRtResponseMapperTest {

  private final Fixtures fixtures = new Fixtures();

  private GnsRtResponseMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new GnsRtResponseMapper();
  }

  @Test
  void simpleFieldsMappingTest() {
    var result = map(fixtures.alert, fixtures.falsePositive);

    assertThat(result)
        .satisfies(r -> assertThat(r.getAlertId()).isEqualTo(fixtures.id))
        .satisfies(r -> assertThat(r.getWatchlistType()).isEqualTo(fixtures.watchlistType))
        .satisfies(r -> assertThat(r.getRecommendationTimestamp()).isEqualTo(fixtures.createdAt));
  }

  private GnsRtResponseAlerts map(GnsRtAlert alert, AlertRecommendation alertRecommendation) {
    return mapper.map(alert, alertRecommendation);
  }

  @Test
  void recommendedActionMappingTest() {
    assertMappedRecommendation(fixtures.falsePositive).isEqualTo(FALSE_POSITIVE);
    assertMappedRecommendation(fixtures.potentialTruePositive).isEqualTo(POTENTIAL_TRUE_POSITIVE);
    assertMappedRecommendation(fixtures.investigate).isEqualTo(INVESTIGATE);
    assertMappedRecommendation(fixtures.hintedFalsePositive).isEqualTo(INVESTIGATE);
    assertMappedRecommendation(fixtures.hintedTruePositive).isEqualTo(INVESTIGATE);
    assertMappedRecommendation(fixtures.partiallyFalsePositive).isEqualTo(INVESTIGATE);
  }

  private AbstractObjectAssert<?, RecommendationEnum> assertMappedRecommendation(
      AlertRecommendation recommendation) {

    var response = map(fixtures.alert, recommendation);

    return assertThat(response).extracting(GnsRtResponseAlerts::getRecommendation);
  }

  private static class Fixtures {

    String id = "id";
    String comment = "comment";
    String createdAt = "2019-01-22T09:21:28";
    String watchlistType = "AM";

    GnsRtAlert alert = alert();

    AlertRecommendation falsePositive =
        recommendation(ACTION_FALSE_POSITIVE);
    AlertRecommendation investigate =
        recommendation(ACTION_INVESTIGATE);
    AlertRecommendation hintedFalsePositive =
        recommendation(ACTION_INVESTIGATE_HINTED_FALSE_POSITIVE);
    AlertRecommendation hintedTruePositive =
        recommendation(ACTION_INVESTIGATE_HINTED_TRUE_POSITIVE);
    AlertRecommendation partiallyFalsePositive =
        recommendation(ACTION_INVESTIGATE_PARTIALLY_FALSE_POSITIVE);
    AlertRecommendation potentialTruePositive =
        recommendation(ACTION_POTENTIAL_TRUE_POSITIVE);

    private GnsRtAlert alert() {
      var alert = new GnsRtAlert();
      alert.setAlertId(id);
      alert.setWatchlistType(watchlistType);
      return alert;
    }

    private AlertRecommendation recommendation(RecommendedAction action) {
      ObjectId objectId = ObjectId.newBuilder()
          .setSourceId(id)
          .setDiscriminator("discriminator")
          .build();

      var recommendation = Recommendation.newBuilder()
          .setAction(action)
          .setComment(comment)
          .setCreatedAt(MoreTimestamps.toTimestamp(Instant.parse(createdAt + "Z")))
          .build();

      return AlertRecommendation.newBuilder()
          .setAlertId(objectId)
          .setRecommendation(recommendation)
          .build();
    }
  }
}
