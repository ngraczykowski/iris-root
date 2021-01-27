package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.List;

@RequiredArgsConstructor
class DefaultStepsConfigurationFactory implements ReconfigurableStepsConfigurationFactory {

  @NonNull
  private final StepMapper stepMapper;

  @Override
  public StepsConfigurationSupplier getStepsConfigurationProvider(
      List<StepConfigurationDto> stepsConfigurationDto) {
    ReconfigurableStepsSupplier result = new ReconfigurableStepsSupplier(stepMapper);
    result.reconfigure(stepsConfigurationDto);
    return result;
  }
}
