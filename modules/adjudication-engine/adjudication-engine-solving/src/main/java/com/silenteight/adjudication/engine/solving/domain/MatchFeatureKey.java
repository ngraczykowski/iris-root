package com.silenteight.adjudication.engine.solving.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class MatchFeatureKey {

  private final long alertId;

  private final long matchId;

  private final String featureName;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    MatchFeatureKey that = (MatchFeatureKey) o;
    return alertId == that.alertId && matchId == that.matchId && Objects.equals(
        featureName, that.featureName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alertId, matchId, featureName);
  }
}
