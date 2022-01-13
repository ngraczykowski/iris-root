package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.job.CreateCategoriesValuesMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createEtlHit;
import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createRegisterAlert;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateCategoriesValuesServiceTest {

  private CreateCategoriesValuesService createCategoryValuesService;

  @BeforeEach
  void setUp() {
    createCategoryValuesService = new CreateCategoriesValuesService(
        new CreateCategoriesValuesMock(),
        List.of(new WatchlistTypeExtractor())
    );
  }

  @Test
  void shouldExtractFeatures() {
    var hit = createEtlHit();
    var categoryValues =
        createCategoryValuesService.createCategoryValues(List.of(hit), createRegisterAlert());
    assertThat(categoryValues.get(0).getCategoryValuesCount()).isEqualTo(1);
  }
}
