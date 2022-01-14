package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.job.CreateCategoriesValuesMock;
import com.silenteight.payments.bridge.svb.newlearning.job.NameAddressCrossmatchUseCaseMock;
import com.silenteight.payments.bridge.svb.newlearning.job.SpecificTerms2UseCaseMock;
import com.silenteight.payments.bridge.svb.newlearning.job.SpecificTermsUseCaseMock;

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
        List.of(new WatchlistTypeCategoryExtractor(),
            new CrossmatchCategoryExtractor(new NameAddressCrossmatchUseCaseMock()),
            new SpecificTermsCategoryExtractor(new SpecificTermsUseCaseMock()),
            new SpecificTerms2CategoryExtractor(new SpecificTerms2UseCaseMock()))
    );
  }

  @Test
  void shouldExtractFeatures() {
    var hit = createEtlHit();
    var categoryValues =
        createCategoryValuesService.createCategoryValues(List.of(hit), createRegisterAlert());
    assertThat(categoryValues.size()).isEqualTo(4);
  }
}
