/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import com.silenteight.iris.qco.domain.model.QcoPolicyStepSolutionOverrideConfiguration;

import java.util.List;

public interface SolutionConfigurationProvider {

  List<QcoPolicyStepSolutionOverrideConfiguration> getSolutionConfigurations();
}
