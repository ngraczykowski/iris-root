package com.silenteight.serp.governance.policy.step.logic.edit;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;

import java.util.Collection;
import java.util.UUID;

@Builder
@Value
class EditStepLogicCommand {

  @NonNull
  UUID stepId;
  @NonNull
  String user;
  @NonNull
  Collection<FeatureLogicConfiguration>  featureLogicConfigurations;
}
