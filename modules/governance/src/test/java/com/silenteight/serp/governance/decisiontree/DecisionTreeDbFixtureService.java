package com.silenteight.serp.governance.decisiontree;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DecisionTreeDbFixtureService {

  private final DecisionTreeRepository decisionTreeRepository;

  public long storeDefaultBranch() {
    DecisionTree decisionTree = new DecisionTree("Default");
    return decisionTreeRepository.save(decisionTree).getId();
  }
}
