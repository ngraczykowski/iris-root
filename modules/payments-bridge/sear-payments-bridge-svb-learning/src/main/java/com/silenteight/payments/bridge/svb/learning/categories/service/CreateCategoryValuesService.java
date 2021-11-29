package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.svb.learning.categories.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
class CreateCategoryValuesService implements CreateCategoryValuesUseCase {

  private final List<CategoryValueExtractor> categoryValueExtractors;
  private final CreateCategoryValuesClient createCategoryValuesClient;

  public void createCategoryValues(
      List<LearningAlert> learningAlerts, List<ReadAlertError> errors) {
    var categoryValueRequests = new ArrayList<CreateCategoryValuesRequest>();
    for (var learningAlert : learningAlerts) {
      try {
        categoryValueRequests.addAll(createValueRequests(Collections.singletonList(learningAlert)));
      } catch (Exception exception) {
        log.error("Failed to create categories for LearningAlert = {} reason = {}",
            learningAlert.getAlertId(), exception.getMessage(), exception);
        errors.add(ReadAlertError
            .builder()
            .alertId(learningAlert.getAlertId())
            .exception(exception)
            .build());
      }
    }

    var request = BatchCreateCategoryValuesRequest.newBuilder()
        .addAllRequests(categoryValueRequests)
        .build();
    createCategoryValuesClient.createCategoriesValues(request);
  }

  private List<CreateCategoryValuesRequest> createValueRequests(
      List<LearningAlert> learningAlerts) {
    return learningAlerts.stream()
        .flatMap(this::extractCategoryValues)
        .map(CreateCategoryValuesService::mapToCreateCategoryValuesRequest)
        .collect(toList());
  }

  private Stream<CategoryValue> extractCategoryValues(LearningAlert alert) {
    var alertName = alert.getAlertName();
    return alert.getMatches().stream()
        .flatMap(learningMatch -> getCategoryValueStream(alertName, learningMatch));
  }

  @Nonnull
  private Stream<CategoryValue> getCategoryValueStream(
      String alertName, LearningMatch learningMatch) {

    return categoryValueExtractors.stream()
        .map(extractor -> {

              if (log.isTraceEnabled()) {
                log.trace("Extracting category value: {}", extractor.getClass().getSimpleName());
              }

              return extractor.extract(alertName, learningMatch);
            }
        );
  }

  private static CreateCategoryValuesRequest mapToCreateCategoryValuesRequest(CategoryValue value) {
    return CreateCategoryValuesRequest
        .newBuilder()
        .setCategory(value.getName())
        .addCategoryValues(value)
        .build();
  }

}
