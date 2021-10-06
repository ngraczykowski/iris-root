package com.silenteight.payments.bridge.categories.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoryValuesClient;

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
      log.trace("Sending create categories vaues request");
    }

    try {
      var response = blockingStub
          .withDeadline(deadline)
          .batchCreateCategoryValues(createCategoryValuesRequest);
      log.trace("Created Categories values");
    } catch (StatusRuntimeException status) {
      log.warn("Request to the datasource service failed", status);
    }
  }
}
