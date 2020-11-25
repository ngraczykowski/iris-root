package com.silenteight.serp.governance.decisiongroup;

import org.springframework.data.repository.Repository;

import java.util.Collection;

interface DecisionGroupRepository extends Repository<DecisionGroup, Long> {

  DecisionGroup save(DecisionGroup decisionGroup);

  Collection<DecisionGroup> findAll();

  boolean existsByName(String decisionGroupName);
}
