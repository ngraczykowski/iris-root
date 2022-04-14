package com.silenteight.adjudication.engine.solving.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.event.FeatureMatchesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchFeatureValuesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchesUpdated;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class AlertSolving {

  private final Map<Long, MatchFeatures> matchesFeatures = new HashMap<>();
  private final transient List<DomainEvent> domainEvents = new LinkedList<>();
  private final long id;
  private final LocalDateTime solvingCreatetime = LocalDateTime.now();

  public AlertSolving(final long id) {
    this.id = id;
  }

  public static AlertSolving empty() {
    return new AlertSolving(0);
  }

  public boolean isEmpty() {
    // TODO refactor when data
    return this.id == 0;
  }

  public long id() {
    return this.id;
  }

  public AlertSolving addMatchesFeatures(List<MatchFeature> matchesFeatures) {
    matchesFeatures.forEach(matchFeature -> {
      var features = this.matchesFeatures.getOrDefault(
          matchFeature.getMatchId(), new MatchFeatures(matchFeature.getMatchId()));
      features.addFeature(matchFeature);
      this.matchesFeatures.putIfAbsent(matchFeature.getMatchId(), features);
    });
    return this;
  }

  public AlertSolving updateMatches(Object object) {
    // TODO
    this.domainEvents.add(new MatchesUpdated());
    this.checkIsCompleted();
    return this;
  }

  public boolean areAlertMatchesSolved() {
    // TODO

    return false;
  }

  public AlertSolving updateFeatureMatches(Object o) {
    // TODO
    this.domainEvents.add(new FeatureMatchesUpdated(null));

    this.checkIsCompleted();
    return this;
  }

  /**
   * Clear pending events
   *
   * @return {@link AlertSolving}
   */
  public AlertSolving clear() {
    this.domainEvents.clear();

    return this;
  }

  /**
   * Fetch pending events <br/>
   * <strong>Events hasn't order, because is doesn't matter</strong>
   *
   * @return copy list of pending {@link DomainEvent} events
   */
  public List<DomainEvent> pendingEvents() {
    return new LinkedList<>(this.domainEvents);
  }

  private void checkIsCompleted() {

  }

  public boolean areAlertsSolved() {
    return false;
  }

  public AlertSolving updateMatchFeatureValues(Object o) {
    this.domainEvents.add(new MatchFeatureValuesUpdated());

    this.checkIsCompleted();
    return this;
  }

  @Override
  public String toString() {
    return "AlertSolving{" +
        "id=" + id +
        ", solvingCreatetime=" + solvingCreatetime +
        '}';
  }
}
