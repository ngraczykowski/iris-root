package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;

import java.util.List;

public interface CategoryDataAccess {

  BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      List<String> matchValuesList);
}
