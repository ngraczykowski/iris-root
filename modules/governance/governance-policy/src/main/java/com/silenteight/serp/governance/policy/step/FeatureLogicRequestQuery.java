package com.silenteight.serp.governance.policy.step;

import com.silenteight.serp.governance.policy.domain.dto.FeaturesLogicDto;

import java.util.UUID;

public interface FeatureLogicRequestQuery {

  FeaturesLogicDto listStepsFeaturesLogic(UUID id);
}
