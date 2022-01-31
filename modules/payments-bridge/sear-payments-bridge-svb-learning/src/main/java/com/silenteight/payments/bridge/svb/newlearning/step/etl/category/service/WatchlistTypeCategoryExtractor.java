package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_WATCHLIST_TYPE;

@Service
class WatchlistTypeCategoryExtractor extends BaseUnstructuredCategoryValueExtractor {

  @Override
  protected String getValue(HitComposite hitComposite) {
    return hitComposite.getWatchlistType().getName();
  }

  @Override
  protected String getCategoryName() {
    return CATEGORY_WATCHLIST_TYPE;
  }

}
