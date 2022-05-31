package com.silenteight.scb.feeding.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.infrastructure.util.MockUtils;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesOut;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryServiceClient;

@Slf4j
public class CategoryServiceClientMock implements CategoryServiceClient {

  @Override
  public BatchCreateCategoriesOut createCategories(
      BatchCreateCategoriesIn request) {
    log.info("MOCK: createCategories");
    MockUtils.randomSleep(10, 20);
    return BatchCreateCategoriesOut.builder().build();
  }
}
