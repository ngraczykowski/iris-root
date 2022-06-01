package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Getter
@Value
public class AlertSolving implements Serializable {

  @Serial private static final long serialVersionUID = 899871569338286453L;
  long alertId;
  long analysisId;
  Map<String, Set<String>> agentFeatures;
  Map<Long, Match> matches;
  LocalDateTime solvingCreatetime = LocalDateTime.now();
  String policy;
  String strategy;

  public AlertSolving(AlertAggregate alertAggregate) {
    alertId = alertAggregate.alertId();
    analysisId = alertAggregate.analysisId();
    matches =
        alertAggregate.matches().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> new Match(alertId, e.getValue())));
    agentFeatures = alertAggregate.agentFeatures();
    policy = alertAggregate.policy();
    strategy = alertAggregate.strategy();
  }

  public AlertSolving(
      final long alertId, final String policy, final String strategy, long analysisId) {
    this.alertId = alertId;
    this.policy = policy;
    this.strategy = strategy;
    this.analysisId = analysisId;
    this.matches = new HashMap<>();
    this.agentFeatures = new HashMap<>();
  }

  public static AlertSolving empty() {
    return new AlertSolving(0, "", "", 0);
  }

  public boolean isEmpty() {
    // TODO refactor when data
    return this.alertId == 0;
  }

  public boolean hasCategories() {
    return matches.values().stream().findFirst().get().getCategories().size() > 0;
  }

  public boolean hasFeatures() {
    return matches.values().stream().findFirst().get().getFeatures().size() > 0;
  }

  public long id() {
    return this.alertId;
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
          .get("categories/" + ResourceName.create(categoryValue.getCategory()).get("categories"))
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

  public List<CategoryMatches> getCategoryMatches() {
    var matchesNames =
        matches.values().stream().map(m -> m.getMatchName(alertId)).collect(toList());

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
