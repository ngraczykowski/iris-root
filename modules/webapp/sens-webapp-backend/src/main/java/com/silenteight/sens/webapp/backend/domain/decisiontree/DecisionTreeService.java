package com.silenteight.sens.webapp.backend.domain.decisiontree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DecisionTreeService {

  private final DecisionTreeRepository repository;

  public List<DecisionTreeView> list() {
    log.debug("Listing Decision Trees");

    return repository.findAll();
  }
}
