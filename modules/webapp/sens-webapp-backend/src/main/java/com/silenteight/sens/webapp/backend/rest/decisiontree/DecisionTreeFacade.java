package com.silenteight.sens.webapp.backend.rest.decisiontree;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.domain.decisiontree.DecisionTreeService;
import com.silenteight.sens.webapp.backend.rest.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.rest.decisiontree.dto.DecisionTreesDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class DecisionTreeFacade {

  private final DecisionTreeService decisionTreeService;

  public DecisionTreesDto list() {
    List<DecisionTreeDto> decisionTrees = decisionTreeService
        .list()
        .stream()
        .map(DecisionTreeDto::new)
        .collect(toList());

    log.debug("Found {} Decision Trees", decisionTrees.size());

    return new DecisionTreesDto(decisionTrees.size(), decisionTrees);
  }
}
