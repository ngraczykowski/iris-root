package com.silenteight.sens.webapp.backend.decisiontree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;

@Slf4j
@RequiredArgsConstructor
class DecisionTreeQuery {

  private final DecisionTreeQueryRepository repository;

  DecisionTreesDto list() {
    log.debug("Listing Decision Trees");

    return repository.findAll();
  }

  DecisionTreeDetailsDto details(long id) {
    log.debug("Finding Decision Tree details. decisionTreeId={}", id);

    return repository.getById(id);
  }
}
