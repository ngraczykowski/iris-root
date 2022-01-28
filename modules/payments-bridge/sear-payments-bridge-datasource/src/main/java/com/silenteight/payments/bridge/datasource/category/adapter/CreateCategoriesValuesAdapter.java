package com.silenteight.payments.bridge.datasource.category.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoryValuesClient;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CreateCategoriesValuesAdapter implements CreateCategoryValuesClient {

  private final CategoryValueServiceBlockingStub blockingStub;

  private final Duration timeout;

  public void createCategoriesValues(BatchCreateCategoryValuesRequest createCategoryValuesRequest) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Sending create categories values request");
    }

    try {
      var response = blockingStub
          .withDeadline(deadline)
          .batchCreateCategoryValues(createCategoryValuesRequest);
      log.trace("Created Categories values");
    } catch (StatusRuntimeException e) {
      log.error("Request to the datasource service failed", e);
    }
  }
}
