package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoryValuesClient;

public class CreateCategoriesValuesMock implements CreateCategoryValuesClient {

  @Override
  public void createCategoriesValues(
      BatchCreateCategoryValuesRequest createCategoryValuesRequest) {

  }
}
