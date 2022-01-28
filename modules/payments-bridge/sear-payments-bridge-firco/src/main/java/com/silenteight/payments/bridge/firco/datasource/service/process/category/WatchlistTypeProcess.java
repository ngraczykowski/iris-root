package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.CategoriesUtils.CATEGORY_NAME_WATCHLIST_TYPE;

@Service
@RequiredArgsConstructor
class WatchlistTypeProcess implements CreateCategoryValueUnstructured {

  @Override
  public CategoryValue createCategoryValue(
      String alertName, String matchName, HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return CategoryValue
        .newBuilder()
        .setName(CATEGORY_NAME_WATCHLIST_TYPE)
        .setAlert(alertName)
        .setMatch(matchName)
        .setSingleValue(getValue(hitAndWatchlistPartyData))
        .build();
  }

  private static String getValue(HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return hitAndWatchlistPartyData.getWatchlistType().toString();
  }
}
