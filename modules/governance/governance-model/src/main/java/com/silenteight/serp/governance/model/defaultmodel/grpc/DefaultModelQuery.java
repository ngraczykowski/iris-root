package com.silenteight.serp.governance.model.defaultmodel.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.Feature;
import com.silenteight.model.api.v1.SolvingModel;
import com.silenteight.serp.governance.model.category.CategoryDto;
import com.silenteight.serp.governance.model.category.CategoryRegistry;
import com.silenteight.serp.governance.model.domain.dto.ModelDto;
import com.silenteight.serp.governance.model.domain.exception.ModelMisconfiguredException;
import com.silenteight.serp.governance.model.featureset.CurrentFeatureSetProvider;
import com.silenteight.serp.governance.model.featureset.FeatureDto;
import com.silenteight.serp.governance.model.featureset.FeatureSetDto;
import com.silenteight.serp.governance.policy.current.CurrentPolicyProvider;
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class DefaultModelQuery {

  static final String DEFAULT_MODEL_NAME = "models/default";

  private final CurrentStrategyProvider currentStrategyProvider;
  private final CurrentPolicyProvider currentPolicyProvider;
  private final CurrentFeatureSetProvider currentFeatureSetProvider;
  private final CategoryRegistry categoryRegistry;

//  public SolvingModel get() throws ModelMisconfiguredException {
//    return SolvingModel.newBuilder()
//        .setName(DEFAULT_MODEL_NAME)
//        .setStrategyName(getStrategyName())
//        .setPolicyName(getPolicyName())
//        .addAllFeatures(getFeatures())
//        .addAllCategories(getCategories())
//        .build();
//  }

  public ModelDto get() throws ModelMisconfiguredException {
    return ModelDto.builder()
        .name(DEFAULT_MODEL_NAME)
        .policyName(getPolicyName())
        .build();
  }

//  private String getStrategyName() throws ModelMisconfiguredException {
//    return currentStrategyProvider.getCurrentStrategy()
//        .orElseThrow(() -> new ModelMisconfiguredException(DEFAULT_MODEL_NAME, "strategyName"));
//  }

  private String getPolicyName() throws ModelMisconfiguredException {
    return currentPolicyProvider.getCurrentPolicy()
        .orElseThrow(() -> new ModelMisconfiguredException(DEFAULT_MODEL_NAME, "policyName"));
  }

//  private List<Feature> getFeatures() {
//    FeatureSetDto currentFeatureSet = currentFeatureSetProvider.getCurrentFeatureSet();
//
//    return currentFeatureSet.getFeatures().stream()
//        .map(DefaultModelQuery::toFeature)
//        .collect(toList());
//  }
//
//  private static Feature toFeature(FeatureDto featureDto) {
//    return Feature.newBuilder()
//        .setName(featureDto.getName())
//        .setAgentConfig(featureDto.getAgentConfig())
//        .build();
//  }
//
//  private List<String> getCategories() {
//    List<CategoryDto> allCategories = categoryRegistry.getAllCategories();
//    return allCategories.stream()
//        .map(CategoryDto::getName)
//        .collect(toList());
//  }
}
