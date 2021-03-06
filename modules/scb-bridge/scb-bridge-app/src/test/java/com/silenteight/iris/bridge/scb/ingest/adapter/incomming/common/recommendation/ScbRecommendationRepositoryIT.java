/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationRepositoryIT.ScbRecommendationRepositoryITConfiguration;
import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.sep.base.testing.containers.PostgresContainer.PostgresTestInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

import java.time.OffsetDateTime;
import java.util.List;

import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EntityScan
@EnableJpaRepositories
@ContextConfiguration(
    classes = ScbRecommendationRepositoryITConfiguration.class,
    initializers = PostgresTestInitializer.class)
class ScbRecommendationRepositoryIT extends BaseDataJpaTest {

  @Autowired
  private ScbRecommendationRepository scbRecommendationRepository;

  private Fixtures fixtures = new Fixtures();

  @Test
  void shouldFailWhenStoreEntityWithoutSystemId() {
    fixtures.simpleRecommendation.setSystemId(null);

    assertStoreFailure(fixtures.simpleRecommendation);
  }

  private void assertStoreFailure(ScbRecommendation entity) {
    assertThrows(DataIntegrityViolationException.class, () -> persistEntity(entity));
  }

  private void persistEntity(ScbRecommendation entity) {
    scbRecommendationRepository.save(entity);

    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void shouldFailWhenStoreEntityWithoutDiscriminator() {
    fixtures.simpleRecommendation.setDiscriminator(null);

    assertStoreFailure(fixtures.simpleRecommendation);
  }

  @Test
  void shouldFailWhenStoreEntityWithoutDecision() {
    fixtures.simpleRecommendation.setDecision(null);

    assertStoreFailure(fixtures.simpleRecommendation);
  }

  @Test
  void shouldFailWhenStoreEntityWithoutComment() {
    fixtures.simpleRecommendation.setComment(null);

    assertStoreFailure(fixtures.simpleRecommendation);
  }

  @Test
  void shouldAllowNullWatchlistId() {
    fixtures.simpleRecommendation.setWatchlistId(null);

    assertDoesNotThrow(() -> persistEntity(fixtures.simpleRecommendation));
  }

  @Test
  void shouldStoreEntityWithAllFieldsCorrectly() {
    ScbRecommendation entity = fixtures.simpleRecommendation;

    persistEntity(entity);

    assertThat(scbRecommendationRepository.findAll()).containsExactly(entity);
  }

  @Test
  void shouldStoreMultipleEntitiesCorrectly() {
    List<ScbRecommendation> entities = asList(
        fixtures.alertLevelRecommendation11,
        fixtures.alertLevelRecommendation12,
        fixtures.alertLevelRecommendation21,
        fixtures.alertLevelRecommendation22,
        fixtures.watchlistLevelRecommendation1);

    entities.forEach(this::persistEntity);

    assertThat(scbRecommendationRepository.findAll()).containsExactlyInAnyOrderElementsOf(entities);
  }

  private static class Fixtures {

    ScbRecommendation simpleRecommendation = createEntity(
        "systemId", "watchlistId", "disc");

    ScbRecommendation alertLevelRecommendation11 = createEntity("systemId1", "disc1");
    ScbRecommendation alertLevelRecommendation21 = createEntity("systemId2", "disc1");
    ScbRecommendation alertLevelRecommendation12 = createEntity("systemId1", "disc2");
    ScbRecommendation alertLevelRecommendation22 = createEntity("systemId2", "disc2");

    ScbRecommendation watchlistLevelRecommendation1 = createEntity(
        "systemId3", "watchlistId3", "disc3");

    private static ScbRecommendation createEntity(
        String systemId, String watchlistId, String discriminator) {

      ScbRecommendation entity = createEntity(systemId, discriminator);
      entity.setWatchlistId(watchlistId);
      return entity;
    }

    private static ScbRecommendation createEntity(String systemId, String discriminator) {
      return createEntity(systemId, discriminator, now());
    }

    private static ScbRecommendation createEntity(
        String systemId, String discriminator, OffsetDateTime recommendedAt) {

      ScbRecommendation entity = new ScbRecommendation();
      entity.setSystemId(systemId);
      entity.setAlertName("alertName");
      entity.setDiscriminator(discriminator);
      entity.setDecision("decision");
      entity.setComment("comment");
      entity.setRecommendedAt(recommendedAt);
      return entity;
    }
  }

  @Configuration
  static class ScbRecommendationRepositoryITConfiguration {

  }
}
