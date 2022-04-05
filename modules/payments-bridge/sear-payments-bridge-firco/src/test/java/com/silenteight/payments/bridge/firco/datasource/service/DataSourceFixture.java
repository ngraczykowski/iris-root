package com.silenteight.payments.bridge.firco.datasource.service;

import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataSourceFixture {

  public static AeAlert createAeAlert() {
    return AeAlert
        .builder()
        .alertId(UUID.randomUUID())
        .alertName("alerts/1")
        .matches(Map.of("matchId", "matches/1"))
        .build();
  }

  public static HitAndWatchlistPartyData createHitAndWatchlistPartyData() {
    return HitAndWatchlistPartyData
        .builder()
        .watchlistType(WatchlistType.ADDRESS)
        .matchingText("matching")
        .id("id")
        .allMatchingFieldValues(
            List.of("matching", "matching2"))
        .allMatchingTexts(List.of("matching", "matching2"))
        .name("name")
        .build();
  }
}
