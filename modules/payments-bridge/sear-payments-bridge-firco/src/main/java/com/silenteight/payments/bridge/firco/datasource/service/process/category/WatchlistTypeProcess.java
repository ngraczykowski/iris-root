package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
@Qualifier("watchlistType")
@RequiredArgsConstructor
class WatchlistTypeProcess implements CategoryValueProcess {

  @Override
  public CategoryValue extract(HitData hitData, String matchValue) {
    WatchlistType watchlistType = hitData.getHitAndWlPartyData().getWatchlistType();
    return CategoryValue
        .newBuilder()
        .setName("categories/watchListType")
        .setMatch(matchValue)
        .setSingleValue(mapWatchListTypeToEntityType(watchlistType).toString())
        .build();
  }

  @Nonnull
  private static EntityType mapWatchListTypeToEntityType(WatchlistType watchlistType) {
    switch (watchlistType) {
      case INDIVIDUAL:
        return EntityType.INDIVIDUAL;
      case COMPANY:
        return EntityType.ORGANIZATION;
      case ADDRESS:
      case VESSEL:
        return EntityType.ENTITY_TYPE_UNSPECIFIED;
      default:
        throw new UnsupportedOperationException();
    }
  }
}
