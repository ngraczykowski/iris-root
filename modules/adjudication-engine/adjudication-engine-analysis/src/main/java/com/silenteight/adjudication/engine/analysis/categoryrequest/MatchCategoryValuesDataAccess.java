package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;

public interface MatchCategoryValuesDataAccess {

  MissingCategoryResult getMissingCategoryValues(long analysisId);

  void createMatchCategoryValues(
      CategoryMap categoryMap, BatchGetMatchesCategoryValuesResponse categoryValues);
}
