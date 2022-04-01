package com.silenteight.qco.domain;

import com.silenteight.qco.domain.model.QcoPolicyStepSolutionOverrideConfiguration;

import java.util.List;

public interface SolutionConfigurationProvider {

  List<QcoPolicyStepSolutionOverrideConfiguration> getSolutionConfigurations();
}
