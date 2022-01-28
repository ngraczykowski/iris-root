package com.silenteight.payments.bridge.datasource.category.port;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;

public interface CreateCategoriesClient {

  void createCategories(BatchCreateCategoriesRequest createCategoriesRequest);
}
