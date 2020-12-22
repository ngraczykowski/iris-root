package com.silenteight.serp.governance.decisiontree;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Collection;
import java.util.Optional;

class InMemoryDecisionTreeRepository extends BasicInMemoryRepository<DecisionTree>
    implements DecisionTreeRepository {


  @Override
  public Optional<DecisionTree> findByName(String treeName) {
    return getInternalStore()
        .values()
        .stream()
        .filter(decisionTree -> decisionTree.getName().equals(treeName))
        .findAny();
  }

  @Override
  public Collection<DecisionTree> findAll() {
    return getInternalStore().values();
  }

  @Override
  public int setAllDefaultForNewGroupsToFalse() {
    getInternalStore()
        .values()
        .forEach(DecisionTree::disableForAutomaticDecisionGroupActivation);
    return getInternalStore().size();
  }

  @Override
  public Optional<DecisionTreeId> findByDefaultForNewGroups() {
    return getInternalStore()
        .values()
        .stream()
        .filter(DecisionTree::isDefaultForNewGroups)
        .map(DecisionTree::getId)
        .map(DecisionTreeId::new)
        .findAny();
  }

  @Override
  public boolean existsById(long id) {
    return getInternalStore()
        .values()
        .stream()
        .map(DecisionTree::getId)
        .anyMatch(t -> t.equals(id));
  }
}
