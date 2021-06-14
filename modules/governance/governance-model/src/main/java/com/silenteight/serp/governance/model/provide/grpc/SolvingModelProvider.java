package com.silenteight.serp.governance.model.provide.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.policy.step.logic.PolicyStepsMatchConditionsNamesProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import java.util.List;

import static com.silenteight.serp.governance.policy.common.PolicyResource.fromResourceName;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SolvingModelProvider {

  private final CurrentStrategyProvider currentStrategyProvider;
  private final PolicyFeatureProvider policiesFeaturesProvider;
  private final PolicyStepsMatchConditionsNamesProvider matchConditionsNamesFromPolicyProvider;

  public SolvingModel get(ModelDto modelDto) {
    List<String> conditionsNamesFromPolicy = matchConditionsNamesFromPolicyProvider
        .getMatchConditionsNames(fromResourceName(modelDto.getPolicy()));

    return SolvingModel
        .newBuilder()
        .setName(modelDto.getName())
        .setStrategyName(getStrategyName(modelDto.getName()))
        .setPolicyName(modelDto.getPolicy())
        .addAllFeatures(getFeatures(conditionsNamesFromPolicy))
        .addAllCategories(getCategories(conditionsNamesFromPolicy))
        .build();
  }

  private String getStrategyName(String modelName) {
    return currentStrategyProvider
        .getCurrentStrategy()
        .orElseThrow(() -> new ModelMisconfiguredException(modelName, "strategyName"));
  }

  private List<Feature> getFeatures(List<String> conditionsFromPolicy) {
    return policiesFeaturesProvider.resolveFeatures(conditionsFromPolicy);
  }

  private static List<String> getCategories(List<String> conditionsFromPolicy) {
    return conditionsFromPolicy
        .stream()
        .filter(name -> name.startsWith("categories/"))
        .collect(toList());
  }
}
