package com.silenteight.sens.webapp.backend.domain.decisiontree;

import java.util.List;

interface DecisionTreeRepository {

  List<DecisionTreeView> findAll();
}
