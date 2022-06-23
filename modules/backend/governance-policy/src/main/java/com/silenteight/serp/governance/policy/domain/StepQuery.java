package com.silenteight.serp.governance.policy.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.details.PolicyStepsCountQuery;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;
import com.silenteight.serp.governance.policy.domain.dto.StepSearchCriteriaDto;
import com.silenteight.serp.governance.policy.domain.dto.StepSearchCriteriaDto.ConditionSearchCriteriaDto;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;
import com.silenteight.serp.governance.policy.step.details.StepRequestQuery;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;
import com.silenteight.serp.governance.policy.step.order.list.PolicyStepsOrderRequestQuery;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
class StepQuery implements
    PolicyStepsRequestQuery,
    PolicyStepsOrderRequestQuery,
    PolicyStepsConfigurationQuery,
    PolicyStepsCountQuery,
    StepRequestQuery {

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
  @Transactional
  public Collection<StepDto> listFilteredSteps(UUID policyId, StepSearchCriteriaDto criteria) {
    Collection<Long> stepIds = stepRepository.findByPolicyUuidAndConditionName(policyId.toString(),
        getSearchCriteriaNames(criteria));
    return stepRepository
        .findAllById(stepIds)
        .stream()
        .filter(s -> s.match(criteria))
        .map(Step::toDto)
        .collect(toList());
  }

  @NotNull
  private static Set<String> getSearchCriteriaNames(StepSearchCriteriaDto criteria) {
    return criteria
        .getConditions()
        .stream()
        .map(ConditionSearchCriteriaDto::getName)
        .collect(toSet());
  }

  @Override
  public Long getPolicyIdForStep(UUID stepId) {
    return stepRepository.getPolicyIdForStep(stepId);
  }

  @Override
  @Transactional
  public List<StepConfigurationDto> listStepsConfiguration(UUID policyId) {
    return getStepsAsStream(policyId)
        .map(Step::toConfigurationDto)
        .collect(toList());
  }

  private Stream<Step> getStepsAsStream(UUID policyId) {
    return getStepsAsStream(policyRepository.getIdByPolicyId(policyId));
  }

  private Stream<Step> getStepsAsStream(long policyId) {
    return stepRepository
        .findAllOrderedByPolicyId(policyId)
        .stream();
  }

  @Override
  public long getStepsCount(UUID policyId) {
    log.info("Counting steps for policyId={}", policyId);
    return stepRepository.countStepsByPolicyId(policyRepository.getIdByPolicyId(policyId));
  }

  @Override
  public StepDto getStep(UUID stepId) {
    return stepRepository.getStepByStepId(stepId).toDto();
  }
}
