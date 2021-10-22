package com.silenteight.payments.bridge.firco.datasource.util;

import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

public class HitDataUtils {

  public static List<HitData> filterHitsData(
      List<HitData> hitsData, Entry<String, String> matchItem) {
    var filteredHitsData = hitsData.stream()
        .filter(hitData -> matchItem.getKey().equals(String.format(
            "%s(%s)",
            hitData.getHitAndWlPartyData().getId(),
            hitData.getHitAndWlPartyData().getTag())))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    return filteredHitsData;


  }
}
