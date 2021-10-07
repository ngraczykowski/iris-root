package com.silenteight.adjudication.engine.analysis.categoryrequest;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.GetCategoryValueResponse;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;

import java.util.List;

interface CategoryServiceClient {

  List<GetCategoryValueResponse> getCategoryValue(MissingCategoryResult missingCategoryResult);
}
