package com.silenteight.serp.governance.model.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

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
  public Collection<Model> findAllByPolicyName(String policyName) {
    return stream()
        .filter(model -> model.hasPolicyName(policyName))
        .collect(toList());
  }

  @Override
  public Optional<Model> findModelByModelVersion(String modelVersion) {
    return stream()
        .filter(model -> model.getModelVersion().equals(modelVersion))
        .findFirst();
  }
}
