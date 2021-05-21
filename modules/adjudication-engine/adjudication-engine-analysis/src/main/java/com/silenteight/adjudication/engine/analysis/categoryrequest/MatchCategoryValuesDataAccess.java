package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.datasource.categories.api.v1.CategoryValue;

import java.util.List;
import java.util.Map;

public interface MatchCategoryValuesDataAccess {

  MissingCategoryResult getMissingCategoryValues(long analysisId);

  void createMatchCategoryValues(
      List<CategoryValue> missingCategoryValues,
      Map<String, Long> categories);
}
