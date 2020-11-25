package com.silenteight.serp.governance.decisiongroup;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DecisionGroupFinder {

  private final DecisionGroupRepository repository;

  @Transactional(readOnly = true)
  public Collection<Long> findAllDecisionGroupIds() {
    return repository
        .findAll()
        .stream()
        .map(DecisionGroup::getId)
        .collect(Collectors.toUnmodifiableList());
  }
}
