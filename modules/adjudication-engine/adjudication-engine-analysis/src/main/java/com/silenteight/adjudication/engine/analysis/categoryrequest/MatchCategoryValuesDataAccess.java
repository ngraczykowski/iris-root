package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.datasource.categories.api.v1.CategoryValue;

import java.util.List;

public interface MatchCategoryValuesDataAccess {

  MissingCategoryResult getMissingCategoryValues(long analysisId);

  void createMatchCategoryValues(CategoryMap categoryMap, List<CategoryValue> categoryValues);
}
