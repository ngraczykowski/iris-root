package com.silenteight.sens.webapp.backend.domain.decisiontree;

import com.silenteight.sens.webapp.common.testing.BasicInMemoryRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

class InMemoryDecisionTreeRepository
    extends BasicInMemoryRepository<DecisionTreeView>
    implements DecisionTreeRepository {

  @Override
  public List<DecisionTreeView> findAll() {
    return stream().collect(toList());
  }

  public DecisionTreeView save(DecisionTreeView decisionTree) {
    getInternalStore().put(decisionTree.getId(), decisionTree);

    return decisionTree;
  }
}
