package com.silenteight.serp.governance.policy.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.details.dto.PolicyDetailsDto;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import java.util.UUID;

@RequiredArgsConstructor
class PolicyDetailsUseCase {

  @NonNull
  private final PolicyDetailsRequestQuery policyDetailsRequestQuery;

  @NonNull
  private final PolicyStepsCountQuery policyStepsCountQuery;

  public PolicyDetailsDto activate(UUID id) {
    PolicyDto policyDto = policyDetailsRequestQuery.details(id);
    long stepsCount = policyStepsCountQuery.getStepsCount(id);
    return new PolicyDetailsDto(policyDto, stepsCount);
  }
}
