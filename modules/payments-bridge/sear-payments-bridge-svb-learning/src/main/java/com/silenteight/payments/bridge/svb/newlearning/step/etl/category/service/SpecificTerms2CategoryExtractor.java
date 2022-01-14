package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SpecificTerms2CategoryExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_SPECIFIC_TERMS_2 = "specificTerms2";

  private final SpecificTerms2UseCase specificTerms2UseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_SPECIFIC_TERMS_2;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return specificTerms2UseCase.invoke(etlHit.toSpecificTermsRequest()).getValue();
  }
}
