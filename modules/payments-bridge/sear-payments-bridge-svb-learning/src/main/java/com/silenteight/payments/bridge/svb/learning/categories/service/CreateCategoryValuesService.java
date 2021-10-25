package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.categories.port.outgoing.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.svb.learning.categories.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateCategoryValuesService implements CreateCategoryValuesUseCase {

  private final List<CategoryValueExtractor> categoryValueExtractors;
  private final CreateCategoryValuesClient createCategoryValuesClient;

  public List<CategoryValue> createCategoryValues(LearningAlert learningAlert) {
    var categoryValues = new ArrayList<CategoryValue>();

    learningAlert
        .getMatches()
        .forEach(match -> categoryValueExtractors.forEach(ce -> categoryValues.add(
            ce.extract(match, String.valueOf(learningAlert.getAlertName())))));

    createCategoryValuesClient.createCategoriesValues(BatchCreateCategoryValuesRequest
        .newBuilder()
        .addAllRequests(
            categoryValues
                .stream()
                .map(cv -> CreateCategoryValuesRequest
                    .newBuilder()
                    .setCategory(cv.getName())
                    .addCategoryValues(cv)
                    .build())
                .collect(
                    toList()))
        .build());

    return categoryValues;
  }

  public void createCategoryValues(List<LearningAlert> learningAlerts) {
    var categoryValueRequests = createValueRequests(learningAlerts);
    var request = BatchCreateCategoryValuesRequest.newBuilder()
        .addAllRequests(categoryValueRequests)
        .build();
    createCategoryValuesClient.createCategoriesValues(request);
  }

  private List<CreateCategoryValuesRequest> createValueRequests(
      List<LearningAlert> learningAlerts) {
    return learningAlerts.stream()
        .flatMap(this::extractCategoryValues)
        .map(this::mapToCreateCategoryValuesRequest)
        .collect(toList());
  }

  private Stream<CategoryValue> extractCategoryValues(LearningAlert alert) {
    return alert.getMatches().stream()
        .flatMap(match ->
            categoryValueExtractors.stream()
                .map(extractor -> extractor.extract(match, alert.getAlertName())));
  }

  private CreateCategoryValuesRequest mapToCreateCategoryValuesRequest(CategoryValue value) {
    return CreateCategoryValuesRequest
        .newBuilder()
        .setCategory(value.getName())
        .addCategoryValues(value)
        .build();
  }

}
