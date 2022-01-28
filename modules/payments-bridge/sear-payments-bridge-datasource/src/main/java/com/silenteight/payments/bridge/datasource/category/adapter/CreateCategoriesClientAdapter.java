package com.silenteight.payments.bridge.datasource.category.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoriesClient;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class CreateCategoriesClientAdapter implements CreateCategoriesClient {

  private final CategoryServiceBlockingStub blockingStub;

  private final Duration timeout;

  public void createCategories(BatchCreateCategoriesRequest createCategoriesRequest) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    log.info("Sending create categories request");

    try {
      var result = blockingStub
          .withDeadline(deadline)
          .batchCreateCategories(createCategoriesRequest);
      log.info("Created Categories with result = {}", result);
    } catch (StatusRuntimeException status) {
      log.warn("Request to the datasource service failed", status);
    }
  }
}
