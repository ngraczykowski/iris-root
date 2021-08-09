package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.CategoryServiceGrpc.CategoryServiceBlockingStub;
import com.silenteight.datasource.categories.api.v1.CategoryValue;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class CategoryServiceClient {

  private final CategoryServiceBlockingStub stub;

  @NonNull
  private final Duration timeout;

  public BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request) {

    BatchGetMatchCategoryValuesResponse response = performRequest(request);

    if (response.getCategoryValuesCount() == 0) {
      // FIXME(ahaczewski): Uncomment this exception, instead of hiding Data Source shit.
      //throw new EmptyCategoryServiceResponseException(stub.getChannel().authority());
    }

    if (response.getCategoryValuesCount() < request.getMatchValuesCount()) {
      log.error("Not all requested category values received: requestedCount={}, receivedCount={}",
          request.getMatchValuesCount(), response.getCategoryValuesCount());

      // FIXME(ahaczewski): Remove this mocked response, instead of hiding Data Source shit.
      var mockedResults = request.getMatchValuesList()
          .stream()
          .map(categoryValueName ->
              CategoryValue.newBuilder()
                  .setSingleValue("DATA_SOURCE_ERROR")
                  .setName(categoryValueName)
                  .build())
          .collect(Collectors.toList());
      return BatchGetMatchCategoryValuesResponse.newBuilder()
          .addAllCategoryValues(mockedResults)
          .build();
    }

    return response;
  }

  private BatchGetMatchCategoryValuesResponse performRequest(
      BatchGetMatchCategoryValuesRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting missing category values: deadline={}, request={}", deadline, request);
    }

    BatchGetMatchCategoryValuesResponse response;

    try {
      response = stub
          .withDeadline(deadline)
          .batchGetMatchCategoryValues(request);
    } catch (StatusRuntimeException status) {
      log.warn("Oh well, data source failed to tell us categories... we'll figuring it"
          + " out ourselves");
      response = BatchGetMatchCategoryValuesResponse.getDefaultInstance();
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
