package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.domain.command.UpdateCommentInput;
import com.silenteight.adjudication.engine.solving.domain.event.FeatureMatchesUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchFeatureUpdated;
import com.silenteight.adjudication.engine.solving.domain.event.MatchesUpdated;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Getter
public class AlertSolving implements Serializable {

  private static final long serialVersionUID = 899871569338286453L;
  // <agent_config,set<features> // Which agent supports specific feature
  long alertId;

  long analysisId;
  Map<String, Set<String>> agentFeatures = new HashMap<>();
  Map<Long, Match> matches = new HashMap<>();
  transient List<DomainEvent> domainEvents = new LinkedList<>();
  LocalDateTime solvingCreatetime = LocalDateTime.now();
  String policy;
  String strategy;

  public AlertSolving(
      final long alertId, final String policy, final String strategy, long analysisId) {
    this.alertId = alertId;
    this.policy = policy;
    this.strategy = strategy;
    this.analysisId = analysisId;
  }

  public static AlertSolving empty() {
    return new AlertSolving(0, "", "", 0);
  }

  public boolean isEmpty() {
    // TODO refactor when data
    return this.alertId == 0;
  }

  public long id() {
    return this.alertId;
  }

  public AlertSolving addMatchesFeatures(List<MatchFeature> matchesFeatures) {

    matchesFeatures.forEach(
        matchFeature -> {
          var match =
              this.matches.getOrDefault(
                  matchFeature.getMatchId(),
                  new Match(matchFeature.getMatchId(), matchFeature.getClientMatchId()));

          match.addFeature(matchFeature);
          this.matches.putIfAbsent(matchFeature.getMatchId(), match);

          if (matchFeature.getAgentConfig() == null) {
            return;
          }

          var agentFeatures =
              this.agentFeatures.getOrDefault(matchFeature.getAgentConfig(), new HashSet<>());
          agentFeatures.add(matchFeature.getFeature());
          this.agentFeatures.put(matchFeature.getAgentConfig(), agentFeatures);
        });
    // event
    this.domainEvents.add(new MatchFeatureUpdated(this));
    return this;
  }

  public AlertSolving addMatchesCategories(List<MatchCategory> matchCategories) {
    matchCategories.forEach(
        matchCategory -> {
          if (matchCategory.getCategory() == null) return;
          var match = this.matches.get(matchCategory.getMatchId());
          match.addCategory(matchCategory);
          this.matches.putIfAbsent(matchCategory.getMatchId(), match);
        });
    return this;
  }

  public Set<String> getAllMatchesNames(String featureName) {
    return this.matches.keySet().stream()
        .filter(key -> !matches.get(key).hasSolvedFeature(featureName))
        .map(
            matchId ->
                ResourceName.create("")
                    .add("alerts", String.valueOf(this.alertId))
                    .add("matches", String.valueOf(matchId))
                    .getPath())
        .collect(toSet());
  }

  public Map<String, Set<String>> getAgentFeatures() {
    return agentFeatures;
  }

  public void updateMatches(Object object) {
    // TODO
    this.domainEvents.add(new MatchesUpdated(this));
    AlertSolving.checkIsCompleted();
  }

  public void updateFeatureMatches(Object o) {
    // TODO
    this.domainEvents.add(new FeatureMatchesUpdated(this));

    AlertSolving.checkIsCompleted();
  }

  /** Clear pending events */
  public void clear() {
    this.domainEvents.clear();
  }

  private static boolean checkIsCompleted() {
    return true;
  }

  /**
   * Fetch pending events <br>
   * <strong>Events hasn't order, because is doesn't matter</strong>
   *
   * @return copy list of pending {@link DomainEvent} events
   */
  public List<DomainEvent> pendingEvents() {
    return new LinkedList<>(this.domainEvents);
  }

  public static boolean areAlertsSolved() {
    return false;
  }

  public AlertSolving updateMatchFeatureValues(
      long matchId, List<FeatureSolution> featureSolutions) {
    // TODO refactor transient domainEvents
    // this.domainEvents.add(new MatchFeatureValuesUpdated(this));

    for (var feature : featureSolutions) {
      matches
          .get(matchId)
          .getFeatures()
          .get(feature.getFeatureName())
          .updateFeatureValue(feature.getSolution(), feature.getReason());
    }

    return this;
  }

  public AlertSolving updateMatchCategoryValues(long matchId, List<CategoryValue> categoryValues) {
    for (var categoryValue : categoryValues) {
      matches
          .get(matchId)
          .getCategories()
          .get(categoryValue.getCategory())
          .updateCategoryValue(categoryValue.getValue());
    }

    return this;
  }

  @Override
  public String toString() {
    return "AlertSolving{" + "id=" + alertId + ", solvingCreatetime=" + solvingCreatetime + '}';
  }

  public boolean isMatchReadyForSolving(long matchId) {
    return matches.get(matchId).hasAllFeaturesSolved()
        && matches.get(matchId).hasAllCategoryValues();
  }

  public List<String> getMatchFeatureNames(long matchId) {
    var features =
        matches.get(matchId).getFeatures().values().stream()
            .map(MatchFeature::getFeature)
            .collect(toList());

    var categories =
        matches.get(matchId).getCategories().values().stream()
            .map(MatchCategory::getCategory)
            .collect(toList());

    return Stream.concat(features.stream(), categories.stream()).collect(toList());
  }

  public List<String> getMatchFeatureVectors(long matchId) {
    var features =
        matches.get(matchId).getFeatures().values().stream()
            .map(MatchFeature::getFeatureValue)
            .collect(toList());

    var categories =
        matches.get(matchId).getCategories().values().stream()
            .map(MatchCategory::getCategoryValue)
            .collect(toList());

    return Stream.concat(features.stream(), categories.stream()).collect(toList());
  }

  public AlertSolving updateMatchSolution(long matchId, String solution, String reson) {
    matches.get(matchId).setSolution(solution, reson);
    return this;
  }

  public boolean isAlertReadyForSolving() {
    boolean isAlertReadyForSolving =
        matches.entrySet().stream().allMatch(entry -> entry.getValue().isSolved());
    log.debug("isAlertReadyForSolving alertId={}", alertId);
    return isAlertReadyForSolving;
  }

  public List<String> getMatchesSolution() {
    return matches.values().stream().map(Match::getSolution).collect(toList());
  }

  public String getPolicy() {
    return this.policy;
  }

  public String getAlertName() {
    return ResourceName.create("").add("alerts", alertId).getPath();
  }

  public String getAnalysisName() {
    return ResourceName.create("").add("analysis", analysisId).getPath();
  }

  public long[] getMatchIds() {
    var matchIds = matches.keySet().toArray(Long[]::new);
    return Arrays.stream(matchIds).mapToLong(Long::longValue).toArray();
  }

  public AlertSolving updateCommentInput(UpdateCommentInput command) {

    return this;
  }

  public List<CategoryMatches> getCategoryMatches() {
    var matchesNames = matches.values().stream().map(Match::getMatchName).collect(toList());

    if (matchesNames.isEmpty()) {
      return List.of();
    }

    return matches.values().stream().findFirst().get().getCategories().values().stream()
        .map(
            mc ->
                CategoryMatches.newBuilder()
                    .addAllMatches(matchesNames)
                    .setCategory(mc.getCategory())
                    .build())
        .collect(toList());
  }
}
