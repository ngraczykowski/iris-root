package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("watchlistType")
@RequiredArgsConstructor
class WatchlistTypeProcess implements CategoryValueProcess {

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {
    WatchlistType watchlistType = hitData.getHitAndWlPartyData().getWatchlistType();
    return CategoryValue
        .newBuilder()
        .setName("categories/watchlistType")
        .setMatch(matchValue)
        .setSingleValue(watchlistType.toString())
        .build();
  }
}
