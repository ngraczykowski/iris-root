package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class WatchlistTypeProcess extends BaseCategoryValueProcess {

  public static final String CATEGORY_WATCHLIST_TYPE = "watchlistType";

  @Override
  protected String getCategoryName() {
    return CATEGORY_WATCHLIST_TYPE;
  }

  @Override
  protected String getValue(HitData hitData) {
    return hitData.getHitAndWlPartyData().getWatchlistType().toString();
  }
}
