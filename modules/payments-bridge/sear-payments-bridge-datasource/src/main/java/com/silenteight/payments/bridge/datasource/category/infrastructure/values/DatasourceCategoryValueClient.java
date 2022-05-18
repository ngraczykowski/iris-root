package com.silenteight.payments.bridge.datasource.category.infrastructure.values;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryValueServiceGrpc.CategoryValueServiceBlockingStub;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CreatedCategoryValue;

import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
final class DatasourceCategoryValueClient {

  private final CategoryValueServiceBlockingStub blockingStub;

  private final Duration timeout;

  private final ExecutorService datasourceThreadPool;

  private static void logRequest(BatchCreateCategoryValuesRequest createCategoryValuesRequest) {
    if (log.isDebugEnabled()) {
      var categoryValues = createCategoryValuesRequest.getRequestsList();
      var categoryNames = categoryValues.stream()
          .map(CreateCategoryValuesRequest::getCategory)
          .distinct()
          .collect(toList());

      log.debug("Sending category values request: count={}, categories={}",
          categoryValues.size(), categoryNames);
    }
  }

  private static void logResponse(BatchCreateCategoryValuesResponse response) {
    if (log.isDebugEnabled()) {

      var matchesSaved = response.getCreatedCategoryValuesList().stream()
          .map(CreatedCategoryValue::getMatch)
          .distinct()
          .collect(toList());

      log.debug(
          "Category values saved for matches, matchCount={}, firstTenMatches={}",
          matchesSaved.size(),
          matchesSaved.subList(0, Math.min(10, matchesSaved.size())));
    }
  }

  void create(BatchCreateCategoryValuesRequest createCategoryValuesRequest) {
    if (!createCategoryValuesRequest.getRequestsList().isEmpty()) {
      sendToDatasource(createCategoryValuesRequest);
    } else {
      log.debug(
          "Batch category value request is empty. Data won't be send to datasource service");
    }
  }

  private void sendToDatasource(BatchCreateCategoryValuesRequest createCategoryValuesRequest) {
    CompletableFuture.runAsync(() -> {
      var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

      logRequest(createCategoryValuesRequest);

      var response = blockingStub
          .withDeadline(deadline)
          .batchCreateCategoryValues(createCategoryValuesRequest);

      logResponse(response);

    }, this.datasourceThreadPool).exceptionally(throwable -> {
      log.error("Request with category values to the datasource service failed", throwable);
      return null;
    });
  }
}

