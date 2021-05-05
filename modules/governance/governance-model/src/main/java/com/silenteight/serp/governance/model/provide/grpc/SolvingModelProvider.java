package com.silenteight.serp.governance.model.provide.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.category.CategoryDto;
import com.silenteight.serp.governance.model.category.CategoryRegistry;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.policy.step.logic.PolicyStepsFeaturesProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import java.util.List;

import static com.silenteight.serp.governance.policy.common.PolicyResource.fromResourceName;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SolvingModelProvider {

  private final CurrentStrategyProvider currentStrategyProvider;
  private final PolicyFeatureProvider policiesFeaturesProvider;
  private final CategoryRegistry categoryRegistry;
  private final PolicyStepsFeaturesProvider featuresFromPolicyProvider;

  public SolvingModel get(ModelDto modelDto) {
    return SolvingModel
        .newBuilder()
        .setName(modelDto.getName())
        .setStrategyName(getStrategyName(modelDto.getName()))
        .setPolicyName(modelDto.getPolicy())
        .addAllFeatures(getFeatures(modelDto.getPolicy()))
        .addAllCategories(getCategories())
        .build();
  }

  private List<Feature> getFeatures(String policyName) {
    List<String> featuresFromPolicy = featuresFromPolicyProvider
        .getFeatures(fromResourceName(policyName));
    return policiesFeaturesProvider.resolveFeatures(featuresFromPolicy);
  }

  private String getStrategyName(String modelName) {
    return currentStrategyProvider
        .getCurrentStrategy()
        .orElseThrow(() -> new ModelMisconfiguredException(modelName, "strategyName"));
  }

  private List<String> getCategories() {
    List<CategoryDto> allCategories = categoryRegistry.getAllCategories();
    return allCategories
        .stream()
        .map(CategoryDto::getName)
        .collect(toList());
  }
}
