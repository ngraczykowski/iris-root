package com.silenteight.serp.governance.policy.solve;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class DefaultStepsProvider implements StepsConfigurationSupplier {

  private static StepMapper stepMapper = new StepMapper();

  private final List<StepConfigurationDto> steps;

  @Override
  public List<Step> get() {
    return steps
        .stream()
        .map(stepMapper::map)
        .collect(toList());
  }
}
