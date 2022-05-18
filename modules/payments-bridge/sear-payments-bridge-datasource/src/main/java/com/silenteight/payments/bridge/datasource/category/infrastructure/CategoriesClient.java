package com.silenteight.payments.bridge.datasource.category.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc.CategoryServiceBlockingStub;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class CategoriesClient {

  private final CategoryServiceBlockingStub blockingStub;

  private final Duration timeout;

  public void create(BatchCreateCategoriesRequest createCategoriesRequest) {
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
