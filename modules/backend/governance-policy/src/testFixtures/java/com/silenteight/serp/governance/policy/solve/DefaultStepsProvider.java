package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DefaultStepsProvider implements StepsSupplier {

  private static final StepMapper STEP_MAPPER = new StepMapper();

  private final List<StepConfigurationDto> steps;

  public DefaultStepsProvider(List<StepConfigurationDto> steps) {
    this.steps = steps;
  }

  @Override
  public List<Step> get() {
    return steps.stream().map(STEP_MAPPER::map).collect(toList());
  }
}
