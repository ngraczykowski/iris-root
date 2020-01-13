package com.silenteight.sens.webapp.backend.decisiontree;

import com.silenteight.sens.webapp.backend.decisiontree.dto.DecisionTreesDto;

public interface DecisionTreeQueryRepository {

  DecisionTreesDto findAll();
}
