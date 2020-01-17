package com.silenteight.sens.webapp.backend.decisiontree;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDetailsDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreeDto;
import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;
import com.silenteight.sens.webapp.backend.decisiontree.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.common.repository.BasicInMemoryRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

class InMemoryDecisionTreeRepository
    extends BasicInMemoryRepository<DecisionTreeDto>
    implements DecisionTreeQueryRepository, DecisionTreeRepository {

  @Override
  public DecisionTreesDto findAll() {
    List<DecisionTreeDto> decisionTrees = stream().collect(toList());
    return new DecisionTreesDto(decisionTrees);
  }

  public DecisionTreeDto save(DecisionTreeDto decisionTree) {
    getInternalStore().put(decisionTree.getId(), decisionTree);
    return decisionTree;
  }

  @Override
  public DecisionTreeDetailsDto getById(long id) {
    if (getInternalStore().containsKey(id))
      return toDetails(getInternalStore().get(id));
    else
      throw new DecisionTreeNotFoundException(id);
  }

  private static DecisionTreeDetailsDto toDetails(DecisionTreeDto decisionTree) {
    return DecisionTreeDetailsDto
        .builder()
        .id(decisionTree.getId())
        .name(decisionTree.getName())
        .status(decisionTree.getStatus())
        .build();
  }
}
