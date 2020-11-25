package com.silenteight.serp.governance.decisiontree;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;

@RequiredArgsConstructor
public class DecisionTreeFacade {

  private final DecisionTreeRepository trees;
  private final GetOrCreateDecisionTreeUseCase getOrCreate;

  @Transactional
  public long getOrCreate(String treeName) {
    return getOrCreate.activate(treeName);
  }

  @Transactional
  public void ensureAtLeastOneDecisionTreeExists() {
    if (trees.findAll().isEmpty())
      createWithRandomName();
  }

  public boolean decisionTreeExists(long id) {
    return trees.existsById(id);
  }

  private void createWithRandomName() {
    trees.save(new DecisionTree(generateTreeName()));
  }

  private static String generateTreeName() {
    return UUID.randomUUID().toString();
  }

  public Optional<DecisionTreeId> findDefaultTreeForNewGroups() {
    return trees.findByDefaultForNewGroups();
  }
}
