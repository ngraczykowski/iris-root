package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SpecificTermsCategoryExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_SPECIFIC_TERMS = "specificTerms";

  private final SpecificTermsUseCase specificTermsUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return specificTermsUseCase.invoke(etlHit.toSpecificTermsRequest()).getValue();
  }
}
