package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_MATCH_TYPE;

@Service
class MatchTypeCategoryExtractor extends BaseUnstructuredCategoryValueExtractor {

  @Override
  protected String getValue(HitComposite hitComposite) {
    return hitComposite.getSolutionType().name();
  }

  @Override
  protected String getCategoryName() {
    return CATEGORY_MATCH_TYPE;
  }

}
