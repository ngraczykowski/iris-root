package com.silenteight.serp.governance.decisiontreesummaryquery;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class TestInMemoryDecisionTreeQueryRepository implements DecisionTreeQueryRepository {

  private final Set<DecisionTreeQuery> store = ConcurrentHashMap.newKeySet();

  @Override
  public Stream<DecisionTreeQuery> findAll() {
    return store.stream();
  }

  @Override
  public Optional<DecisionTreeQuery> findByDecisionTreeId(long treeId) {
    return store.stream()
        .filter(decisionTreeQuery -> decisionTreeQuery.getDecisionTreeId() == treeId)
        .findAny();
  }

  @Override
  public Optional<DecisionTreeQuery> findByDecisionGroupName(String decisionGroupName) {
    return store.stream()
        .filter(query -> query.getDecisionGroupNames().contains(decisionGroupName))
        .findAny();
  }

  public void store(DecisionTreeToStore decisionTreeToStore) {
    DecisionTreeQuery decisionTreeQuery = DecisionTreeQuery.builder()
        .decisionTreeId(decisionTreeToStore.getDecisionTreeId())
        .decisionTreeName(decisionTreeToStore.getDecisionTreeName())
        .decisionGroupNames(decisionTreeToStore.getDecisionGroupNames())
        .build();

    store.add(decisionTreeQuery);
  }

  @Value
  @Builder
  public static class DecisionTreeToStore {

    long decisionTreeId;

    private String decisionTreeName;
    List<String> decisionGroupNames;
  }
}
