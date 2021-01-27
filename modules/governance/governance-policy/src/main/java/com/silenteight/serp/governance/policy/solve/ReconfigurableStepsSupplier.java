package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ReconfigurableStepsSupplier implements StepsConfigurationSupplier {

  @NonNull
  private final StepMapper stepMapper;

  private List<Step> stepsConfiguration;

  @Override
  public List<Step> get() {
    return stepsConfiguration;
  }

  void reconfigure(List<StepConfigurationDto> dtos) {
    stepsConfiguration = dtos.stream().map(stepMapper::map).collect(toList());
  }
}
