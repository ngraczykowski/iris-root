package com.silenteight.serp.governance.policy.step.logic;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;

import java.util.Collection;
import java.util.UUID;

@Builder
@Data
public class EditStepLogicCommand {

  @NonNull
  private final UUID stepId;
  @NonNull
  private final String user;
  @NonNull
  private final Collection<FeatureLogicConfiguration>  featureLogicConfigurations;
}
