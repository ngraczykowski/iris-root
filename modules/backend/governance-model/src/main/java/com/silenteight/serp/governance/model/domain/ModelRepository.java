package com.silenteight.serp.governance.model.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

interface ModelRepository extends Repository<Model, Long> {

  Model save(Model model);

  Optional<Model> findByModelId(UUID modelId);

  Collection<Model> findAllByPolicyName(String policyName);

  Optional<Model> findModelByModelVersion(String modelVersion);
}
