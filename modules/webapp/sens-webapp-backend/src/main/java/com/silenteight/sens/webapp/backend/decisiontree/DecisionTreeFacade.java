package com.silenteight.sens.webapp.backend.decisiontree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;

@Slf4j
@RequiredArgsConstructor
public class DecisionTreeFacade {

  private final DecisionTreeQuery decisionTreeQuery;
  private final DecisionTreeService decisionTreeService;

  public DecisionTreesDto list() {
    DecisionTreesDto decisionTrees = decisionTreeQuery.list();

    log.debug("Found {} Decision Trees", decisionTrees.getTotal());

    return decisionTrees;
  }

  public DecisionTreeDetailsDto details(long id) {
    DecisionTreeDetailsDto details = decisionTreeQuery.details(id);

    log.debug("Found Decision Tree details. decisionTreeId={}", id);

    return details;
  }
}
