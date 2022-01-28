package com.silenteight.payments.bridge.datasource.category.port;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;

public interface CreateCategoryValuesClient {

  void createCategoriesValues(BatchCreateCategoryValuesRequest createCategoryValuesRequest);
}
