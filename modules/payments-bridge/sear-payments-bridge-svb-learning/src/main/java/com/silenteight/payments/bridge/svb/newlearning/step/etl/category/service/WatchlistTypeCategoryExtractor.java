package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.stereotype.Service;

@Service
class WatchlistTypeCategoryExtractor extends BaseCategoryValueExtractor {

  public static final String CATEGORY_WATCHLIST_TYPE = "watchlistType";

  @Override
  protected String getCategoryName() {
    return "watchlistType";
  }

  @Override
  protected String getValue(EtlHit etlHit) {
    return etlHit.getWatchlistType().getName();
  }
}
