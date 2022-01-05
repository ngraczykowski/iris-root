package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service.FeatureFixture.createEtlHit;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateFeatureServiceTest {

  private CreateFeatureService createFeatureService;

  @BeforeEach
  void setUp() {
    createFeatureService = new CreateFeatureService(
        List.of(new GeoFeatureExtractorService(), new IdentificationMismatchExtractor()));
  }

  @Test
  void shouldExtractFeatures() {
    var hit = createEtlHit();
    var features = createFeatureService.createFeatureInputs(hit);
    assertThat(features.get(hit.getMatchId()).size()).isEqualTo(2);
  }
}
