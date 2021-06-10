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

  private final CategoryServiceBlockingStub categoryServiceStub;

  @NonNull
  private final Duration timeout;

  public BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request) {

    return categoryServiceStub
        .withDeadline(Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS))
        .batchGetMatchCategoryValues(request);
  }
}
