package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendation.WatchlistLevelConvertRecommendationException;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScbRecommendationTest {

  @Test
  void shouldConvertToRecommendationDto() {
    ScbRecommendation scbRecommendation = ScbRecommendation.builder()
        .systemId("externalId")
        .discriminator("discriminator")
        .decision("decision")
        .comment("comment")
        .build();

    RecommendationDto recommendationDto = scbRecommendation.toRecommendationDto();

    assertThat(recommendationDto.getExternalId()).isEqualTo(scbRecommendation.getSystemId());
    assertThat(recommendationDto.getDiscriminator()).isEqualTo(
        scbRecommendation.getDiscriminator());
    assertThat(recommendationDto.getDecision()).isEqualTo(scbRecommendation.getDecision());
    assertThat(recommendationDto.getComment()).isEqualTo(scbRecommendation.getComment());
  }

  @Test
  void shouldFailWhenConvertToRecommendationDtoWatchlistLevelAlert() {
    ScbRecommendation entity = new ScbRecommendation();
    entity.setWatchlistId("watchlistId");

    assertThrows(
        WatchlistLevelConvertRecommendationException.class,
        entity::toRecommendationDto);
  }

  @Test
  void shouldConvertToRecommendationDtoWhenAlertRecommendationIsAlertLevel() {
    ScbRecommendation entity = new ScbRecommendation();

    assertDoesNotThrow(entity::toRecommendationDto);
  }
}
