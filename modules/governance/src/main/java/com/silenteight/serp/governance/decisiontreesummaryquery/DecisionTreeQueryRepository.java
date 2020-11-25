package com.silenteight.serp.governance.decisiontreesummaryquery;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;

interface DecisionTreeQueryRepository extends Repository<DecisionTreeQuery, Long> {

  Stream<DecisionTreeQuery> findAll();

  Optional<DecisionTreeQuery> findByDecisionTreeId(long treeId);

  @Query("SELECT q"
      + " FROM DecisionTreeQuery q"
      + " WHERE :decisionGroupName IN elements(q.decisionGroupNames)")
  Optional<DecisionTreeQuery> findByDecisionGroupName(String decisionGroupName);
}
