package com.silenteight.payments.bridge.datasource.category.port.incoming;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;

import java.util.List;

public interface GetMatchCategoryValuesUseCase {

  BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      List<String> matchValuesList);
}
