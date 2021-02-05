package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.details.PolicyDetailsRequestQuery;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;
import com.silenteight.serp.governance.policy.list.ListPoliciesRequestQuery;
import com.silenteight.serp.governance.policy.solve.InUsePolicyQuery;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.PolicyState.IN_USE;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
class PolicyQuery implements ListPoliciesRequestQuery, InUsePolicyQuery, PolicyDetailsRequestQuery {

  @NonNull
  private final PolicyRepository policyRepository;

  @Override
  @NotNull
  public Collection<PolicyDto> list(Collection<PolicyState> states) {
    return policyRepository
        .findAllByStateIn(states)
        .stream()
        .map(Policy::toDto)
        .collect(toList());
  }

  @Override
  public Collection<PolicyDto> listAll() {
    return policyRepository
        .findAll()
        .stream()
        .map(Policy::toDto)
        .collect(toList());
  }

  @Override
  public Optional<UUID> getPolicyInUse() {
    return policyRepository.findByStateEquals(IN_USE).map(Policy::getPolicyId);
  }

  @Override
  public PolicyDto details(UUID id) {
    return policyRepository.getByPolicyId(id).toDto();
  }
}
