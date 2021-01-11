package com.silenteight.serp.governance.policy.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepDto;
import com.silenteight.serp.governance.policy.step.PolicyStepsOrderRequestQuery;
import com.silenteight.serp.governance.policy.step.PolicyStepsRequestQuery;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class StepQuery implements PolicyStepsRequestQuery, PolicyStepsOrderRequestQuery {

  private final StepRepository stepRepository;
  private final PolicyRepository policyRepository;

  @Override
  public List<UUID> listStepsOrder(UUID policyId) {
    Long id = policyRepository.getIdByPolicyId(policyId);
    //TODO(kdzieciol): Fix this https://silent8.atlassian.net/browse/WEB-425
    return stepRepository.findAllByPolicyId(id).stream().map(Step::getStepId).collect(toList());
  }

  @Override
  public Collection<StepDto> listSteps(UUID policyId) {
    Long id = policyRepository.getIdByPolicyId(policyId);
    return stepRepository.findAllByPolicyId(id).stream().map(Step::toDto).collect(toList());
  }
}
