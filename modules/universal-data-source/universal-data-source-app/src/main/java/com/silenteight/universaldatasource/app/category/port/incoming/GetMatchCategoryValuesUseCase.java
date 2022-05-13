package com.silenteight.universaldatasource.app.category.port.incoming;

import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;

import java.util.List;

public interface GetMatchCategoryValuesUseCase {

  BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      List<CategoryMatches> matchValuesList);
}
