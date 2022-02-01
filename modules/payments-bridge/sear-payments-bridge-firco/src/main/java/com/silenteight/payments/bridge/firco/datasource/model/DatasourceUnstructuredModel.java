package com.silenteight.payments.bridge.firco.datasource.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

@Value
@Builder
public class DatasourceUnstructuredModel {

  String alertName;

  String matchName;

  HitAndWatchlistPartyData hitAndWatchlistPartyData;

  public String getTag() {
    return hitAndWatchlistPartyData.getTag();
  }

  public WatchlistType getWatchlistType() {
    return hitAndWatchlistPartyData.getWatchlistType();
  }
}
