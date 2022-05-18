package com.silenteight.payments.bridge.firco.datasource.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitDataUtils {

  public static List<HitData> filterHitsData(
      List<HitData> hitsData, Entry<String, String> matchItem) {

    return hitsData.stream()
        .filter(Objects::nonNull)
        .filter(hitData -> matchItem.getKey().equals(hitData.getMatchId()))
        .collect(Collectors.toList());
  }
}
