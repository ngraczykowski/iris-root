package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendation.WatchlistLevelConvertRecommendationException;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ScbRecommendationTest {

  @Test
  void shouldCreateFromRecommendationDtoCorrectly() {
    RecommendationDto recommendationDto = RecommendationDto.builder()
        .externalId("externalId")
        .discriminator("discriminator")
        .decision("decision")
        .comment("comment")
        .date(OffsetDateTime.now())
        .build();
    ScbRecommendation entity = new ScbRecommendation(recommendationDto);

    assertThat(entity.getSystemId()).isEqualTo(recommendationDto.getExternalId());
    assertThat(entity.getDiscriminator()).isEqualTo(recommendationDto.getDiscriminator());
    assertThat(entity.getDecision()).isEqualTo(recommendationDto.getDecision());
    assertThat(entity.getComment()).isEqualTo(recommendationDto.getComment());
    assertThat(entity.getWatchlistId()).isNull();
  }

  @Test
  void shouldConvertToRecommendationDto() {
    RecommendationDto recommendationDto = RecommendationDto.builder()
        .externalId("externalId")
        .discriminator("discriminator")
        .decision("decision")
        .comment("comment")
        .date(OffsetDateTime.now())
        .build();

    ScbRecommendation entity = new ScbRecommendation(recommendationDto);

    assertThat(entity.toRecommendationDto()).isEqualTo(recommendationDto);
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
