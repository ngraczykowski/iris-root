package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.CategoryMap;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.GetCategoryValueResponse;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;

import java.util.List;

public interface MatchCategoryValuesDataAccess {

  MissingCategoryResult getMissingCategoryValues(long analysisId);

  void createMatchCategoryValues(
      CategoryMap categoryMap, List<GetCategoryValueResponse> categoryValues);
}
