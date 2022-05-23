package com.silenteight.scb.feeding.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesIn;
import com.silenteight.universaldatasource.api.library.category.v2.BatchCreateCategoriesOut;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryServiceClient;

import org.apache.commons.lang3.RandomUtils;

@Slf4j
public class CategoryServiceClientMock implements CategoryServiceClient {

  @Override
  public BatchCreateCategoriesOut createCategories(
      BatchCreateCategoriesIn request) {
    log.info("MOCK: createCategories");
    randomSleep();
    return BatchCreateCategoriesOut.builder().build();
  }

  private static void randomSleep() {
    try {
      Thread.sleep(RandomUtils.nextInt(10, 20));
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }
}
