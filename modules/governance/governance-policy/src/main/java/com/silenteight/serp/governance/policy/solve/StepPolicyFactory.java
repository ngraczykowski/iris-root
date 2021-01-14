package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.dto.FeatureConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

class StepPolicyFactory {

  private List<Step> steps = new ArrayList<>();

  List<Step> getSteps() {
    return steps;
  }

  void reconfigure(List<StepConfigurationDto> dtos) {
    steps = dtos
        .stream()
        .map(StepPolicyFactory::mapToStep)
        .collect(toList());
  }

  private static Step mapToStep(StepConfigurationDto dto) {
    return new Step(dto.getSolution(), dto.getId(), mapToFeatureLogics(dto.getFeatureLogics()));
  }

  private static Collection<FeatureLogic> mapToFeatureLogics(
      Collection<FeatureLogicConfigurationDto> dtos) {

    return dtos
        .stream()
        .map(StepPolicyFactory::mapToFeatureLogic)
        .collect(toList());
  }

  private static FeatureLogic mapToFeatureLogic(FeatureLogicConfigurationDto dto) {
    return FeatureLogic.builder()
        .count(dto.getCount())
        .features(mapToFeatures(dto.getFeatures()))
        .build();
  }

  private static Collection<Feature> mapToFeatures(Collection<FeatureConfigurationDto> dtos) {
    return dtos
        .stream()
        .map(StepPolicyFactory::mapToFeature)
        .collect(toList());
  }

  private static Feature mapToFeature(FeatureConfigurationDto dto) {
    return Feature.builder()
        .name(dto.getName())
        .values(dto.getValues())
        .build();
  }
}
