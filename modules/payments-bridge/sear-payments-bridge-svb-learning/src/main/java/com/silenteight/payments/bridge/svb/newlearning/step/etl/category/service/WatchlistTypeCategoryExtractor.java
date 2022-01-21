package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_WATCHLIST_TYPE;

@Service
class WatchlistTypeCategoryExtractor extends BaseCategoryValueExtractor {

  @Override
  protected String getCategoryName() {
    return CATEGORY_WATCHLIST_TYPE;
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return etlHit.getWatchlistType().getName();
  }
}
