package com.silenteight.serp.governance.model.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Optional;

class InMemoryModelRepository
    extends BasicInMemoryRepository<Model>
    implements ModelRepository {

  @Override
  public Optional<Model> findByPolicyName(String policyName) {
    return stream()
        .filter(model -> model.getPolicyName().equals(policyName))
        .findFirst();
  }
}
