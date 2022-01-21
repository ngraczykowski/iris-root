package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_SPECIFIC_TERMS_2;

@Service
@RequiredArgsConstructor
class SpecificTerms2CategoryExtractor extends BaseUnstructuredCategoryValueExtractor {

  private final SpecificTerms2UseCase specificTerms2UseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS_2;
  }

  @Override
  protected String getValue(HitComposite hitComposite) {
    return specificTerms2UseCase.invoke(hitComposite.toSpecificTermsRequest()).getValue();
  }
}
