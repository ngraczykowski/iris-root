package com.silenteight.serp.governance.decisiontree;

import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
class GetOrCreateDecisionTreeUseCase {

  private final DecisionTreeRepository trees;

  @Transactional
  long activate(String treeName) {
    DecisionTree tree = trees
        .findByName(treeName)
        .orElseGet(() -> createDecisionTree(treeName));

    enableTreeForAutomaticDecisionGroupActivation(tree);

    return tree.getId();
  }

  private DecisionTree createDecisionTree(String treeName) {
    return trees.save(new DecisionTree(treeName));
  }

  private void enableTreeForAutomaticDecisionGroupActivation(DecisionTree tree) {
    trees.setAllDefaultForNewGroupsToFalse();
    tree.enableForAutomaticDecisionGroupActivation();
    trees.save(tree);
  }

}
