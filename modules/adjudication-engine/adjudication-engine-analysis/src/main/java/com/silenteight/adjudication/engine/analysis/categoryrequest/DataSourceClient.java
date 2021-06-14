package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceBlockingStub;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
class DataSourceClient {

  private final CategoryServiceBlockingStub stub;

  @NonNull
  private final Duration timeout;

  public BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request) {

    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting missing category values: deadline={}, request={}", deadline, request);
    }

    var response = stub
        .withDeadline(deadline)
        .batchGetMatchCategoryValues(request);

    if (response.getCategoryValuesCount() == 0) {
      throw new EmptyCategoryServiceResponseException(stub.getChannel().authority());
    }

    if (response.getCategoryValuesCount() < request.getMatchValuesCount()) {
      log.warn("Not all requested category values received: requestedCount={}, receivedCount={}",
          request.getMatchValuesCount(), response.getCategoryValuesCount());
    }

    if (log.isTraceEnabled()) {
      log.trace("Received category values: response={}", response);
    }

    return response;
  }

  private static final class EmptyCategoryServiceResponseException extends RuntimeException {

    private static final long serialVersionUID = 7750138753922426634L;

    EmptyCategoryServiceResponseException(String authority) {
      super("Received empty response from Category Service at [" + authority + "]");
    }
  }
}
