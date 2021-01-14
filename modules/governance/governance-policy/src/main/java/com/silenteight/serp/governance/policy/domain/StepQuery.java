package com.silenteight.serp.governance.policy.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;
import com.silenteight.serp.governance.policy.step.PolicyStepsOrderRequestQuery;
import com.silenteight.serp.governance.policy.step.PolicyStepsRequestQuery;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class StepQuery implements
    PolicyStepsRequestQuery,
    PolicyStepsOrderRequestQuery,
    PolicyStepsConfigurationQuery {

  private final StepRepository stepRepository;
  private final PolicyRepository policyRepository;

  @Override
  public List<UUID> listStepsOrder(UUID policyId) {
    Long id = policyRepository.getIdByPolicyId(policyId);
    return stepRepository
        .findAllOrderedStepsIdsByPolicyId(id)
        .stream()
        .map(UUID::fromString)
        .collect(toList());
  }

  @Override
  public Collection<StepDto> listSteps(UUID policyId) {
    return getStepsAsStream(policyId)
        .map(Step::toDto)
        .collect(toList());
  }

  @Override
  public List<StepConfigurationDto> listStepsConfiguration(UUID policyId) {
    return getStepsAsStream(policyId)
        .map(Step::toConfigurationDto)
        .collect(toList());
  }

  private Stream<Step> getStepsAsStream(UUID policyId) {
    Long id = policyRepository.getIdByPolicyId(policyId);
    return stepRepository
        .findAllOrderedByPolicyId(id)
        .stream();
  }
}
