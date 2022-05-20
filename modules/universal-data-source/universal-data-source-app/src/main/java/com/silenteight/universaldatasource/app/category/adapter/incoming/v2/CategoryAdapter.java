package com.silenteight.universaldatasource.app.category.adapter.incoming.v2;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.*;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import javax.validation.Valid;

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

  BatchCreateCategoriesResponse batchCreateCategories(
      @Valid BatchCreateCategoriesRequest request) {
    return createCategoriesUseCase.createCategories(request.getCategoriesList());
  }

  BatchGetMatchesCategoryValuesResponse batchGetMatchCategoryValues(
      @Valid BatchGetMatchesCategoryValuesRequest request) {
    return getMatchCategoryValuesUseCase.batchGetMatchCategoryValues(
        request.getCategoryMatchesList());
  }

  BatchCreateCategoryValuesResponse batchCreateCategoryValues(
      @Valid BatchCreateCategoryValuesRequest request) {
    return createCategoryValuesUseCase.createCategoryValues(request.getRequestsList());
  }

  CreateCategoryValuesResponse createCategoryValues(
      @Valid CreateCategoryValuesRequest request) {

    var batchCreateCategoryValuesResponse =
        createCategoryValuesUseCase.createCategoryValues(List.of(request));
    return CreateCategoryValuesResponse.newBuilder()
        .addAllCreatedCategoryValues(
            batchCreateCategoryValuesResponse.getCreatedCategoryValuesList())
        .build();
  }
}
