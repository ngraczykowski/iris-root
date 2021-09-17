package com.silenteight.universaldatasource.app.category.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.*;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class CategoryAdapter {

  private final ListAvailableCategoriesUseCase listAvailableCategoriesUseCase;
  private final GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase;
  private final CreateCategoriesUseCase createCategoriesUseCase;
  private final CreateCategoryValuesUseCase createCategoryValuesUseCase;

  ListCategoriesResponse listCategories() {
    return listAvailableCategoriesUseCase.getAvailableCategories();
  }

  BatchCreateCategoriesResponse batchCreateCategories(BatchCreateCategoriesRequest request) {
    return createCategoriesUseCase.createCategories(request.getCategoriesList());
  }

  BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchesCategoryValuesRequest request) {
    return getMatchCategoryValuesUseCase.batchGetMatchCategoryValues(
        request.getCategoryMatchesList());
  }

  BatchCreateCategoryValuesResponse batchCreateCategoryValues(
      BatchCreateCategoryValuesRequest request) {
    return createCategoryValuesUseCase.addCategoryValues(request.getRequestsList());
  }

  CreateCategoryValuesResponse createCategoryValues(CreateCategoryValuesRequest request) {
    var batchCreateCategoryValuesResponse =
        createCategoryValuesUseCase.addCategoryValues(List.of(request));
    return CreateCategoryValuesResponse.newBuilder()
        .addAllCreatedCategoryValues(
            batchCreateCategoryValuesResponse.getCreatedCategoryValuesList())
        .build();
  }
}
