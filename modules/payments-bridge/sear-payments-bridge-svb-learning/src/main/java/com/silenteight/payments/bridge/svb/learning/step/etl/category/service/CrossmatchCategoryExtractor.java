package com.silenteight.payments.bridge.svb.learning.step.etl.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.NameAddressCrossmatchUseCase;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_CROSSMATCH;

@Service
@RequiredArgsConstructor
class CrossmatchCategoryExtractor extends BaseCategoryValueExtractor {

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
