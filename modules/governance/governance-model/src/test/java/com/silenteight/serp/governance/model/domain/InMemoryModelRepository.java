package com.silenteight.serp.governance.model.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Optional;
import java.util.UUID;

class InMemoryModelRepository
    extends BasicInMemoryRepository<Model>
    implements ModelRepository {

  @Override
  public Optional<Model> findByModelId(UUID modelId) {
    return stream()
        .filter(model -> model.hasModelId(modelId))
        .findFirst();
  }

  @Override
  public Optional<Model> findByPolicyName(String policyName) {
    return stream()
        .filter(model -> model.hasPolicyName(policyName))
        .findFirst();
  }
}
