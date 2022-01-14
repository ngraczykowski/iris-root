package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

@Service
class MatchTypeCategoryExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_MATCH_TYPE = "matchType";

  @Override
  protected String getCategoryName() {
    return CATEGORY_MATCH_TYPE;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return etlHit.getSolutionType().name();
  }
}
