package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;
import com.silenteight.serp.governance.policy.saved.SavedPolicyRequestQuery;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
class PolicyQuery implements SavedPolicyRequestQuery {

  @NonNull
  private final PolicyRepository policyRepository;

  @Override
  @NotNull
  public Collection<PolicyDto> listSaved() {
    return policyRepository
        .findAll()
        .stream()
        .map(Policy::toDto)
        .collect(toList());
  }
}
