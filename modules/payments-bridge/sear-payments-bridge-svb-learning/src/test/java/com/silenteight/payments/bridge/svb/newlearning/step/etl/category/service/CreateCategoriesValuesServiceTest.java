package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.job.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createEtlHit;
import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createHitComposite;
import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createRegisterAlert;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateCategoriesValuesServiceTest {

  private CreateCategoriesValuesService createCategoryValuesService;

  @BeforeEach
  void setUp() {
    createCategoryValuesService =
        new CreateCategoriesValuesService(
            new CreateCategoriesValuesMock(),
            List.of(
                new CrossmatchCategoryExtractor(new NameAddressCrossmatchUseCaseMock()),
                new HistoricalRiskCategoryExtractor(new HistoricalRiskAssessmentUseCaseMock())),
            List.of(
                new MessageStructureCategoryExtractor(),
                new MatchTypeCategoryExtractor(),
                new WatchlistTypeCategoryExtractor(),
                new SpecificTermsCategoryExtractor(new SpecificTermsUseCaseMock()),
                new SpecificTerms2CategoryExtractor(new SpecificTerms2UseCaseMock()))
        );
  }

  @Test
  void shouldExtractFeatures() {
    var hit = createEtlHit();
    var categoryValues =
        createCategoryValuesService.createCategoryValues(List.of(hit), createRegisterAlert());
    assertThat(categoryValues.size()).isEqualTo(2);

    var hitComposite = createHitComposite();
    var unstructuredCategoryValues =
        createCategoryValuesService.createUnstructuredCategoryValues(
            List.of(hitComposite), createRegisterAlert());
    assertThat(unstructuredCategoryValues.size()).isEqualTo(5);
  }
}
