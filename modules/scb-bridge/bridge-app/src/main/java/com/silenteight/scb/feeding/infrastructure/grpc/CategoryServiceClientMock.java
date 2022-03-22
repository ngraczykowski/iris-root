package com.silenteight.scb.feeding.infrastructure.grpc;

import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesOut;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryServiceClient;

public class CategoryServiceClientMock implements CategoryServiceClient {

  @Override
  public BatchCreateCategoriesOut createCategories(
      BatchCreateCategoriesIn request) {
    return BatchCreateCategoriesOut.builder().build();
  }
}
