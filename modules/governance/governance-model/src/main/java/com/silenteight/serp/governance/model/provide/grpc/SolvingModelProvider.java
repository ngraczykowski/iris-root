package com.silenteight.serp.governance.model.provide.grpc;

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
import com.silenteight.serp.governance.strategy.CurrentStrategyProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class SolvingModelProvider {

  private final CurrentStrategyProvider currentStrategyProvider;
  private final CurrentFeatureSetProvider currentFeatureSetProvider;
  private final CategoryRegistry categoryRegistry;

  public SolvingModel get(ModelDto modelDto) throws ModelMisconfiguredException {
    return SolvingModel.newBuilder()
                       .setName(modelDto.getName())
                       .setStrategyName(getStrategyName(modelDto.getName()))
                       .setPolicyName(modelDto.getPolicyName())
                       .addAllFeatures(getFeatures())
                       .addAllCategories(getCategories())
                       .build();
  }

  private String getStrategyName(String modelName) throws ModelMisconfiguredException {
    return currentStrategyProvider.getCurrentStrategy()
                                  .orElseThrow(() -> new ModelMisconfiguredException(modelName,
                                                                                     "strategyName"));
  }

  private List<Feature> getFeatures() {
    FeatureSetDto currentFeatureSet = currentFeatureSetProvider.getCurrentFeatureSet();

    return currentFeatureSet.getFeatures().stream()
                            .map(SolvingModelProvider::toFeature)
                            .collect(toList());
  }

  private static Feature toFeature(FeatureDto featureDto) {
    return Feature.newBuilder()
                  .setName(featureDto.getName())
                  .setAgentConfig(featureDto.getAgentConfig())
                  .build();
  }

  private List<String> getCategories() {
    List<CategoryDto> allCategories = categoryRegistry.getAllCategories();
    return allCategories.stream()
                        .map(CategoryDto::getName)
                        .collect(toList());
  }
}
