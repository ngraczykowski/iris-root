package com.silenteight.serp.governance.model.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface ModelRepository extends Repository<Model, Long> {

  Optional<Model> findByPolicyName(String policyName);

  Model save(Model model);
}
