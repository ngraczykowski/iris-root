package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;
import com.silenteight.serp.governance.policy.list.ListPolicyRequestQuery;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
class PolicyQuery implements ListPolicyRequestQuery {

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
}
