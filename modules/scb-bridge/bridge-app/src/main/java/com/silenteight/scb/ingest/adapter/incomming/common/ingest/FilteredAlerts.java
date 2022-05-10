package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.util.List;
import java.util.function.Predicate;

public class FilteredAlerts {

  private final Predicate<Alert> hasDecision = Alert::hasDecision;

  private final Predicate<Alert> hasRecommendation;

  private final List<Alert> alerts;

  FilteredAlerts(
      List<Alert> alerts,
      Predicate<Alert> hasRecommendation) {
    this.alerts = alerts;
    this.hasRecommendation = memoized(hasRecommendation);
  }

  private static <T> Predicate<T> memoized(Predicate<T> predicate) {
    return CacheBuilder.newBuilder()
        .build(CacheLoader.from(predicate::test))
        ::getUnchecked;
  }

  public List<Alert> alertsWithDecisions() {
    return filter(hasDecision);
  }

  private List<Alert> filter(Predicate<Alert> predicate) {
    return alerts.stream()
        .filter(predicate)
        .toList();
  }

  public List<Alert> alertsWithDecisionAndWithRecommendation() {
    return filter(hasDecision.and(hasRecommendation));
  }

  public List<Alert> alertsWithoutDecisions() {
    return filter(hasDecision.negate());
  }

  public List<Alert> alertsWithDecisionAndWithoutRecommendation() {
    return filter(hasDecision.and(hasRecommendation.negate()));
  }

}
