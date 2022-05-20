package com.silenteight.hsbc.bridge.domain;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.hsbc.bridge.match.MatchIdComposite;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Builder
@Getter
public class AlertMatchIdComposite {

  private long alertInternalId;
  private String alertExternalId;
  private OffsetDateTime alertTime;
  private Collection<MatchIdComposite> matchIds;

  public Collection<String> getMatchExternalIds() {
    return matchIds.stream()
        .map(MatchIdComposite::getExternalId)
        .collect(Collectors.toSet());
  }
}
