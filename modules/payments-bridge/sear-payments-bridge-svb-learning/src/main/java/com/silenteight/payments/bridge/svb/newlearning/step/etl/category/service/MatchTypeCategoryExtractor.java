package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_MATCH_TYPE;

@Service
class MatchTypeCategoryExtractor extends BaseCategoryValueExtractor {

  @Override
  protected String getCategoryName() {
    return CATEGORY_MATCH_TYPE;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return etlHit.getSolutionType().name();
  }
}
