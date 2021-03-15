package com.silenteight.hsbc.bridge.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

@Builder
@Getter
public class AlertMatchIdComposite {
  private long alertId;
  private Collection<Long> matchIds;
}
