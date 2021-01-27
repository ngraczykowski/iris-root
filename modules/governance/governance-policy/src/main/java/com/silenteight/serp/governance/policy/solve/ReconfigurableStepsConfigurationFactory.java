package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.List;

public interface ReconfigurableStepsConfigurationFactory {

  StepsConfigurationSupplier getStepsConfigurationProvider(
      List<StepConfigurationDto> stepsConfigurationDto);
}
