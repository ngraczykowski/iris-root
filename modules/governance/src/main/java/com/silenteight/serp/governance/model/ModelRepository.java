package com.silenteight.serp.governance.model;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

interface ModelRepository extends Repository<ModelEntity, Long> {

  ModelEntity save(ModelEntity model);

  Collection<ModelEntity> findAll();

  Optional<ModelEntity> findBySignature(String modelSignature);

  ModelEntity getBySignature(String modelSignature);
}
