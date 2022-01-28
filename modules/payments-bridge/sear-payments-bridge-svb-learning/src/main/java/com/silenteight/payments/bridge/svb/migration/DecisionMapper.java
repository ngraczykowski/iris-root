package com.silenteight.payments.bridge.svb.migration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.migration.DecisionEntry.DecisionKey;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DecisionMapper {

  @NonNull
  private final Map<DecisionKey, DecisionEntry> decisionMap;

  @NonNull
  private final DecisionEntry defaultValue;

  public String map(List<String> previous, String current) {
    return previous.stream()
        .map(prev -> new DecisionKey(prev, current))
        .filter(decisionMap::containsKey)
        .map(decisionMap::get)
        .findFirst()
        .orElseGet(() ->
          decisionMap.getOrDefault(
              new DecisionKey("*", current), defaultValue))
        .getDecision();
  }

}
