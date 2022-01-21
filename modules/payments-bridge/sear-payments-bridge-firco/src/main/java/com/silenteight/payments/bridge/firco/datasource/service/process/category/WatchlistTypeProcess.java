package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_WATCHLIST_TYPE;

@Service
@RequiredArgsConstructor
class WatchlistTypeProcess extends BaseCategoryValueProcess {

  @Override
  protected String getCategoryName() {
    return CATEGORY_WATCHLIST_TYPE;
  }

  @Override
  protected String getValue(HitData hitData) {
    return hitData.getHitAndWlPartyData().getWatchlistType().toString();
  }
}
