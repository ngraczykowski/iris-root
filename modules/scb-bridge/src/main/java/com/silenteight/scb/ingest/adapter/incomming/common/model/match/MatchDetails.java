package com.silenteight.scb.ingest.adapter.incomming.common.model.match;

import lombok.Builder;

import java.util.Collection;
import java.util.Set;

public record MatchDetails(
    Collection<String> matchedApNames,
    Set<String> matchingTexts,
    String matchName) {

  @Builder
  public MatchDetails {}
}
