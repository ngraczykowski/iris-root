package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

class StepMapper {

  public Step map(StepConfigurationDto dto) {
    return new Step(
        dto.getSolution(), dto.getId(), dto.getTitle(), mapToFeatureLogics(dto.getFeatureLogics()));
  }

  private static Collection<FeatureLogic> mapToFeatureLogics(
      Collection<FeatureLogicConfigurationDto> dtos) {

    return dtos
        .stream()
        .map(StepMapper::mapToFeatureLogic)
        .collect(toList());
  }

  private static FeatureLogic mapToFeatureLogic(FeatureLogicConfigurationDto dto) {
    return new FeatureLogic(dto.getCount(), mapToFeatures(dto.getFeatures()));
  }

  private static Collection<MatchCondition> mapToFeatures(
      Collection<MatchConditionConfigurationDto> dtos) {

    return dtos
        .stream()
        .map(StepMapper::mapToMatchCondition)
        .collect(toList());
  }

  private static MatchCondition mapToMatchCondition(MatchConditionConfigurationDto dto) {
    return new MatchCondition(dto.getName(), dto.getCondition(), dto.getValues());
  }
}
