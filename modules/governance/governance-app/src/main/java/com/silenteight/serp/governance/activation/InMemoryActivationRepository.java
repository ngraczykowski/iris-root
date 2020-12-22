package com.silenteight.serp.governance.activation;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Optional;
import java.util.function.Predicate;

class InMemoryActivationRepository extends BasicInMemoryRepository<Activation>
    implements ActivationRepository {


  @Override
  public Optional<Activation> findByDecisionGroupId(long decisionGroupId) {
    return getInternalStore()
        .values()
        .stream()
        .filter(activation -> activation.getDecisionGroupId() == decisionGroupId)
        .findAny();
  }

  @Override
  public void deleteByDecisionTreeIdAndDecisionGroupId(long treeId, long groupId) {
    Predicate<Activation> treeIdMatches = activation -> activation.getDecisionTreeId() == treeId;
    Predicate<Activation> groupIdMatches = activation -> activation.getDecisionGroupId() == groupId;

    getInternalStore().values().removeIf(treeIdMatches.and(groupIdMatches));
  }
}
