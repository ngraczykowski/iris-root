package com.silenteight.serp.governance.decisiongroup;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Collection;

class InMemoryDecisionGroupRepository extends BasicInMemoryRepository<DecisionGroup>
    implements DecisionGroupRepository {

  @Override
  public Collection<DecisionGroup> findAll() {
    return getInternalStore().values();
  }

  @Override
  public boolean existsByName(String name) {
    return getInternalStore()
        .values()
        .stream()
        .anyMatch(decisionGroup -> decisionGroup.getName().equals(name));
  }
}
