package com.silenteight.payments.bridge.svb.learning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_WATCHLIST_TYPE;

@Service
class WatchlistTypeCategoryExtractor extends BaseUnstructuredCategoryValueExtractor {

  @Override
  protected String getValue(HitComposite hitComposite) {
    return hitComposite.getWatchlistType().toString();
  }

  @Override
  protected String getCategoryName() {
    return CATEGORY_WATCHLIST_TYPE;
  }

}
