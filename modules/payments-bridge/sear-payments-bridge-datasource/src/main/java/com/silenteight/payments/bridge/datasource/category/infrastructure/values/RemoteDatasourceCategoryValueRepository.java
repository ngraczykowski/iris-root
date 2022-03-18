package com.silenteight.payments.bridge.datasource.category.infrastructure.values;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.datasource.category.CategoryValueRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
class RemoteDatasourceCategoryValueRepository implements CategoryValueRepository {

  private final DatasourceCategoryValueClient datasourceCategoryClientValueClient;

  @Override
  public void save(List<CategoryValue> categoryValues) {
    var categoryValuesRequests = createCategoryValuesRequests(categoryValues);
    var request = createBatchCreateCategoryValuesRequest(categoryValuesRequests);
    datasourceCategoryClientValueClient.create(request);
  }

  private static BatchCreateCategoryValuesRequest createBatchCreateCategoryValuesRequest(
      List<CreateCategoryValuesRequest> categoryValuesRequests) {
    return BatchCreateCategoryValuesRequest.newBuilder()
        .addAllRequests(categoryValuesRequests)
        .build();
  }

  private static List<CreateCategoryValuesRequest> createCategoryValuesRequests(
      List<CategoryValue> categoryValues) {
    return categoryValues.stream()
        .map(RemoteDatasourceCategoryValueRepository::createRequest)
        .collect(toList());
  }

  private static CreateCategoryValuesRequest createRequest(CategoryValue categoryValue) {
    return CreateCategoryValuesRequest.newBuilder()
        .setCategory(categoryValue.getName())
        .addCategoryValues(categoryValue)
        .build();
  }

}
