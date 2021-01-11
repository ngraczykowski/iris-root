package com.silenteight.serp.governance.policy.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;
import com.silenteight.serp.governance.policy.saved.SavedPolicyRequestQuery;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional(readOnly = true)
class PolicyQuery implements SavedPolicyRequestQuery {

  private final PolicyRepository policyRepository;

  @Override
  public Collection<PolicyDto> listSaved() {
    return doListSaved();
  }

  @NotNull
  private List<PolicyDto> doListSaved() {
    return policyRepository
        .findAll()
        .stream()
        .map(Policy::toDto)
        .collect(toList());
  }
}
