package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Collection;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class InMemoryPolicyRepository extends BasicInMemoryRepository<Policy>
    implements PolicyRepository {

  @Override
  public Policy getByPolicyId(UUID policyId) {
    return stream()
        .filter(policy -> policy.getPolicyId() == policyId)
        .findFirst()
        .orElseThrow();
  }

  @Override
  public Collection<Policy> findAll() {
    return stream().collect(toList());
  }

  @Override
  public Long getIdByPolicyId(UUID policyId) {
    return getByPolicyId(policyId).getId();
  }
}
