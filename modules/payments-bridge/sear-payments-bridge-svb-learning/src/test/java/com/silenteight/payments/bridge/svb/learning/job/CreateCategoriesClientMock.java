package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoriesClient;

class CreateCategoriesClientMock implements CreateCategoriesClient {

  @Override
  public void createCategories(
      BatchCreateCategoriesRequest createCategoriesRequest) {

  }
}
