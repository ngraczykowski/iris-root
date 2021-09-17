package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryValueDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class CreateCategoryValuesService implements CreateCategoryValuesUseCase {

  private final CategoryValueDataAccess categoryValueDataAccess;

  @Override
  public BatchCreateCategoryValuesResponse addCategoryValues(
      List<CreateCategoryValuesRequest> categoryValues) {

    var createdCategoryValues =
        categoryValueDataAccess.saveAll(categoryValues);

    if (log.isDebugEnabled()) {
      log.debug("Saved category values: categoryValuesCount={}", createdCategoryValues.size());
    }

    return BatchCreateCategoryValuesResponse.newBuilder()
        .addAllCreatedCategoryValues(createdCategoryValues)
        .build();
  }
}
