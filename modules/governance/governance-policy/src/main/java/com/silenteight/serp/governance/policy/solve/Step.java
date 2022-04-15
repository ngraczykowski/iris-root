package com.silenteight.serp.governance.policy.solve;

import lombok.Value;

import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.*;
import javax.annotation.concurrent.ThreadSafe;

import static java.util.List.copyOf;
import static java.util.stream.Collectors.toSet;

@Value
@ThreadSafe
class Step {

  private static final String CATEGORIES_PREFIX = "categories/";
  private static final String FEATURES_PREFIX = "features/";

  FeatureVectorSolution solution;
  UUID stepId;
  String stepTitle;
  Collection<FeatureLogic> featureLogics;
  List<String> features = new ArrayList<>();
  List<String> categories = new ArrayList<>();

  Step(
      FeatureVectorSolution solution,
      UUID stepId,
      String stepTitle,
      Collection<FeatureLogic> featureLogics) {

    this.solution = solution;
    this.stepId = stepId;
    this.stepTitle = stepTitle;
    this.featureLogics = copyOf(featureLogics);

    resolveFeaturesAndCategories(collectMatchConditionNames());
  }

  private Set<String> collectMatchConditionNames() {
    return featureLogics.stream()
        .map(Step::toMatchConditionNames)
        .flatMap(Set::stream)
        .collect(toSet());
  }

  private static Set<String> toMatchConditionNames(FeatureLogic featureLogic) {
    return featureLogic.getFeatures()
        .stream()
        .map(MatchCondition::getName)
        .collect(toSet());
  }

  private void resolveFeaturesAndCategories(Set<String> matchConditionNames) {
    for (String matchConditionName: matchConditionNames) {

      if (isFeature(matchConditionName))
        features.add(matchConditionName);

      if (isCategory(matchConditionName))
        categories.add(matchConditionName);
    }
  }

  private static boolean isFeature(String matchConditionName) {
    return matchConditionName.startsWith(FEATURES_PREFIX);
  }

  private static boolean isCategory(String matchConditionName) {
    return matchConditionName.startsWith(CATEGORIES_PREFIX);
  }

  boolean matchesFeatureValues(Map<String, String> featureValuesByName) {
    return featureLogics.stream().allMatch(featureLogic -> featureLogic.match(featureValuesByName));
  }

  SolveResponse getResponse() {
    return new SolveResponse(solution, features, categories, stepId, stepTitle);
  }
}
