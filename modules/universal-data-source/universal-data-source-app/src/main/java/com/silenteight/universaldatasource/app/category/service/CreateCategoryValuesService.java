package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CreatedCategoryValue;
import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.ValidateCategoryValueUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryValueDataAccess;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
class CreateCategoryValuesService implements CreateCategoryValuesUseCase {

  private final CategoryValueDataAccess categoryValueDataAccess;

  private final ValidateCategoryValueUseCase validateCategoryValue;

  @Timed(
      value = "uds.category.use_cases",
      extraTags = {"action", "createCategoryValues"},
      histogram = true,
      percentiles = {0.5, 0.95, 0.99})
  @Override
  public BatchCreateCategoryValuesResponse createCategoryValues(
      List<CreateCategoryValuesRequest> categoryValues) {

    logRequest(categoryValues);

    var createdCategoryValues = saveCategoryValues(categoryValues);

    logRepositoryResponse(categoryValues, createdCategoryValues);

    return BatchCreateCategoryValuesResponse.newBuilder()
        .addAllCreatedCategoryValues(createdCategoryValues)
        .build();
  }

  private static void logRequest(List<CreateCategoryValuesRequest> categoryValues) {
    if (log.isDebugEnabled()) {
      var categoryNames =
          categoryValues.stream()
              .map(CreateCategoryValuesRequest::getCategory)
              .distinct()
              .collect(toList());

      var matchNames =
          categoryValues.stream()
              .map(CreateCategoryValuesRequest::getCategoryValuesList)
              .flatMap(Collection::stream)
              .map(CategoryValue::getMatch)
              .distinct()
              .limit(10)
              .collect(toList());

      log.debug(
          "Saving category values: count={}, categories={}, firstTenMatches={}",
          categoryValues.size(),
          categoryNames,
          matchNames);
    }
  }

  private List<CreatedCategoryValue> saveCategoryValues(
      List<CreateCategoryValuesRequest> categoryValues) {
    validateCategoryValueBatch(categoryValues);
    return categoryValueDataAccess.saveAll(categoryValues);
  }

  private void validateCategoryValueBatch(List<CreateCategoryValuesRequest> categoryValues) {
    validateCategoryValue.isValid(categoryValues);
  }

  private static void logRepositoryResponse(
      List<CreateCategoryValuesRequest> categoryValues,
      List<CreatedCategoryValue> createdCategoryValues) {

    if (log.isDebugEnabled()) {
      var categoryNames =
          categoryValues.stream()
              .map(CreateCategoryValuesRequest::getCategory)
              .distinct()
              .collect(toList());

      log.debug(
          "Saved category values: count={}, categories={}",
          createdCategoryValues.size(),
          categoryNames);
    }
  }
}
