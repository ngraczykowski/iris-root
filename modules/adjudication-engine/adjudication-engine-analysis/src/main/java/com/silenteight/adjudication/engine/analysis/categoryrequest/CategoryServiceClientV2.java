package com.silenteight.adjudication.engine.analysis.categoryrequest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.GetCategoryValueResponse;
import com.silenteight.adjudication.engine.analysis.categoryrequest.domain.MissingCategoryResult;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchGetMatchesCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryMatches;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;

import io.grpc.Deadline;
import io.grpc.StatusRuntimeException;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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

    if (response.getCategoryValuesCount() < request.getCategoryMatchesCount()) {
      log.error("Not all requested category values received: requestedCount={}, receivedCount={}",
          request.getCategoryMatchesCount(), response.getCategoryValuesCount());

      // FIXME(ahaczewski): Remove this mocked response, instead of hiding Data Source shit.
      return buildMockResponse(request, response);
    }

    return response;
  }

  @Nonnull
  private BatchGetMatchesCategoryValuesResponse buildMockResponse(
      BatchGetMatchesCategoryValuesRequest request,
      BatchGetMatchesCategoryValuesResponse response) {

    var mockedResponseBuilder = response.toBuilder();

    var responseCategoryMatches = response.getCategoryValuesList().stream()
        .collect(groupingBy(CategoryValue::getName, mapping(CategoryValue::getMatch, toSet())));

    request.getCategoryMatchesList()
        .stream()
        .flatMap(categoryMatch -> buildNotReceived(responseCategoryMatches, categoryMatch))
        .forEach(mockedResponseBuilder::addCategoryValues);

    return mockedResponseBuilder.build();
  }

  private Stream<CategoryValue> buildNotReceived(
      Map<String, Set<String>> responseCategoryMatches, CategoryMatches categoryMatch) {

    var category = categoryMatch.getCategory();
    var hasReceivedCategory = responseCategoryMatches.containsKey(category);

    if (!hasReceivedCategory) {
      return categoryMatch.getMatchesList().stream()
          .map(m -> CategoryValue.newBuilder()
              .setName(category)
              .setMatch(m)
              .setSingleValue("DATA_SOURCE_ERROR")
              .build());
    } else {
      var receivedMatches = responseCategoryMatches.get(category);

      return categoryMatch.getMatchesList().stream()
          .filter(m -> !receivedMatches.contains(m))
          .map(m -> CategoryValue.newBuilder()
              .setName(category)
              .setMatch(m)
              .setSingleValue("DATA_SOURCE_ERROR")
              .build());
    }
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
}
