package com.silenteight.payments.bridge.svb.learning.categories.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.svb.learning.categories.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.payments.bridge.svb.learning.categories.port.outgoing.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                    Collectors.toList()))
        .build());

    return categoryValues;
  }
}
