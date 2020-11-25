package com.silenteight.serp.governance.decisiontreesummaryquery;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.governance.DecisionTreeSummary;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
public class DecisionTreeSummaryFinder {

  private final DecisionTreeQueryRepository repository;

  @Transactional(readOnly = true)
  public Collection<DecisionTreeSummary> getAll() {
    return repository
        .findAll()
        .map(DecisionTreeQuery::summarize)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public DecisionTreeSummary getById(long treeId) {
    return findById(treeId)
        .orElseThrow(() -> new EntityNotFoundException("DecisionTree not found: " + treeId));
  }

  @Transactional(readOnly = true)
  public DecisionTreeSummary getByDecisionGroupName(String decisionGroupName) {
    return findByDecisionGroupName(decisionGroupName)
        .orElseThrow(() -> new EntityNotFoundException(
            "DecisionTree for given decision group not found: " + decisionGroupName));
  }

  @Transactional(readOnly = true)
  public Optional<DecisionTreeSummary> findById(long treeId) {
    return repository.findByDecisionTreeId(treeId)
        .map(DecisionTreeQuery::summarize);
  }

  @Transactional(readOnly = true)
  public Optional<DecisionTreeSummary> findByDecisionGroupName(String decisionGroupName) {
    return repository.findByDecisionGroupName(decisionGroupName)
        .map(DecisionTreeQuery::summarize);
  }
}
