package com.silenteight.serp.governance.model;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Collection;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;

class InMemoryModelRepository extends BasicInMemoryRepository<ModelEntity>
    implements ModelRepository {

  @Override
  public Collection<ModelEntity> findAll() {
    return getInternalStore().values();
  }

  @Override
  public Optional<ModelEntity> findBySignature(String modelSignature) {
    return getInternalStore()
        .values()
        .stream().filter(modelEntity -> modelEntity.getSignature().equals(modelSignature))
        .findFirst();
  }

  @Override
  public ModelEntity getBySignature(String modelSignature) {
    return findBySignature(modelSignature).orElseThrow(EntityNotFoundException::new);
  }
}
