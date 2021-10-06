package com.silenteight.payments.bridge.firco.datasource.util;

import com.silenteight.payments.bridge.svb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HitDataUtils {

  public static List<HitData> filterHitsData(
      List<HitData> hitsData, Entry<String, String> matchItem) {
    return hitsData.stream()
        .filter(hitData -> matchItem.getKey().equals(
            Optional.of(hitData)
                .map(HitData::getHitAndWlPartyData)
                .map(HitAndWatchlistPartyData::getId)
                .orElse(null)))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
