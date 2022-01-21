package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_SPECIFIC_TERMS;

@Service
@RequiredArgsConstructor
class SpecificTermsCategoryExtractor extends BaseUnstructuredCategoryValueExtractor {

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS;
  }

  @Override
  protected String getValue(
      HitComposite hitComposite) {
    return specificTermsUseCase.invoke(hitComposite.toSpecificTermsRequest()).getValue();
  }
}
