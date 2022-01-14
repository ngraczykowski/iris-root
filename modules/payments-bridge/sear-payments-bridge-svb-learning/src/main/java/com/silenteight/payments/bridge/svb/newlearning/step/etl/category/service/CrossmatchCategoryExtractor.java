package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CrossmatchCategoryExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_CROSSMATCH = "crossmatch";
  private final NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase;

  @Override
  protected String getCategoryName() {
    return CATEGORY_CROSSMATCH;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return nameAddressCrossmatchUseCase
        .call(etlHit.toNameAddressCrossmatchAgentRequest())
        .toString();
  }
}
