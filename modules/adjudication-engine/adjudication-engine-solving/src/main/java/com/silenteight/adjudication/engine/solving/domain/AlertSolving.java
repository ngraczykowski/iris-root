package com.silenteight.adjudication.engine.solving.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.domain.event.FeatureMatchesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchFeatureUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchFeatureValuesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchesUpdated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AlertSolving {

  //<agent_config,set<features> // Which agent supports specific feature
  private final Map<String, Set<String>> agentFeatures = new HashMap<>();
  private final Map<Long, Match> matches = new HashMap<>();
  private final transient List<DomainEvent> domainEvents = new LinkedList<>();
  private final long alertId;
  private final LocalDateTime solvingCreatetime = LocalDateTime.now();

  public AlertSolving(final long alertId) {
    this.alertId = alertId;
  }

  public static AlertSolving empty() {
    return new AlertSolving(0);
  }

  public boolean isEmpty() {
    // TODO refactor when data
    return this.alertId == 0;
  }

  public long id() {
    return this.alertId;
  }

  public AlertSolving addMatchesFeatures(List<MatchFeature> matchesFeatures) {
    matchesFeatures.forEach(matchFeature -> {
      var features = this.matches.getOrDefault(
          matchFeature.getMatchId(), new Match(matchFeature.getMatchId()));
      features.addFeature(matchFeature);
      this.matches.putIfAbsent(matchFeature.getMatchId(), features);

      var agentFeatures =
          this.agentFeatures.getOrDefault(matchFeature.getAgentConfig(), new HashSet<>());
      agentFeatures.add(matchFeature.getFeature());
      this.agentFeatures.put(matchFeature.getAgentConfig(), agentFeatures);
    });
    // event
    this.domainEvents.add(new MatchFeatureUpdated(this));
    return this;
  }


  public Set<String> getAllMatchesNames() {
    return this.matches
        .keySet()
        .stream()
        .map(matchId -> ResourceName
            .create("")
            .add("alerts", String.valueOf(this.alertId))
            .add("matches", String.valueOf(matchId))
            .getPath())
        .collect(
            Collectors.toSet());
  }

  public Map<String, Set<String>> getAgentFeatures() {
    return agentFeatures;
  }

  public void updateMatchFeatureValue(long matchId, String featureName, String featureValue) {
    this.matches.get(matchId).getFeatures().get(featureName).updateFeatureValue(featureValue);
  }

  public AlertSolving updateMatches(Object object) {
    // TODO
    this.domainEvents.add(new MatchesUpdated(this));
    this.checkIsCompleted();
    return this;
  }

  public boolean areAlertMatchesSolved() {
    // TODO

    return false;
  }

  public AlertSolving updateFeatureMatches(Object o) {
    // TODO
    this.domainEvents.add(new FeatureMatchesUpdated(this));

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
    this.domainEvents.add(new MatchFeatureValuesUpdated(this));

    this.checkIsCompleted();
    return this;
  }

  @Override
  public String toString() {
    return "AlertSolving{" +
        "id=" + alertId +
        ", solvingCreatetime=" + solvingCreatetime +
        '}';
  }
}
