package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.GetCategoryValueResponse;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class CategoryServiceClientV2 implements CategoryServiceClient {

  private final CategoryValueServiceBlockingStub stub;

  @NonNull
  private final Duration timeout;


  @Override
  public List<GetCategoryValueResponse> getCategoryValue(
      MissingCategoryResult missingCategoryResult) {
    var response =
        batchGetMatchCategoryValues(missingCategoryResult.toBatchGetMatchCategoryValuesRequestV2());
    return response
        .getCategoryValuesList()
        .stream()
        .map(GetCategoryValueResponse::fromCategoryValueV2)
        .collect(toList());
  }

  BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchesCategoryValuesRequest request) {

    BatchGetMatchesCategoryValuesResponse response = performRequest(request);

    if (response.getCategoryValuesCount() == 0) {
      // FIXME(ahaczewski): Uncomment this exception, instead of hiding Data Source shit.
      // throw new EmptyCategoryServiceResponseException(stub.getChannel().authority());
    }

    if (response.getCategoryValuesCount() < request.getCategoryMatchesCount()) {
      log.error("Not all requested category values received: requestedCount={}, receivedCount={}",
          request.getCategoryMatchesCount(), response.getCategoryValuesCount());

      // FIXME(ahaczewski): Remove this mocked response, instead of hiding Data Source shit.
      var mockedResults = request.getCategoryMatchesList()
          .stream()
          .map(categoryValueName ->
              CategoryValue.newBuilder()
                  .setSingleValue("DATA_SOURCE_ERROR")
                  .setName(categoryValueName.getCategory())
                  .build())
          .collect(toList());
      return BatchGetMatchesCategoryValuesResponse.newBuilder()
          .addAllCategoryValues(mockedResults)
          .build();
    }

    return response;
  }

  private BatchGetMatchesCategoryValuesResponse performRequest(
      BatchGetMatchesCategoryValuesRequest request) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (log.isTraceEnabled()) {
      log.trace("Requesting missing category values: deadline={}, request={}", deadline, request);
    }

    BatchGetMatchesCategoryValuesResponse response;

    try {
      response = stub
          .withDeadline(deadline)
          .batchGetMatchesCategoryValues(request);
    } catch (StatusRuntimeException status) {
      log.warn("Request to the categories service failed", status);
      response = BatchGetMatchesCategoryValuesResponse.getDefaultInstance();
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
