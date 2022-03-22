package com.silenteight.scb.feeding.infrastructure.grpc;

import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoryValuesIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoryValuesOut;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryValuesServiceClient;

public class CategoryValuesServiceClientMock implements CategoryValuesServiceClient {

  @Override
  public BatchCreateCategoryValuesOut createCategoriesValues(
      BatchCreateCategoryValuesIn request) {
    return BatchCreateCategoryValuesOut.builder().build();
  }
}
