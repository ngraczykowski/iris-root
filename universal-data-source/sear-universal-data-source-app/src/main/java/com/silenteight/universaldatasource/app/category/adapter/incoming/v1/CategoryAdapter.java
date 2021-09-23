package com.silenteight.universaldatasource.app.category.adapter.incoming.v1;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;
import com.silenteight.universaldatasource.app.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;

import javax.validation.Valid;

@RequiredArgsConstructor
class CategoryAdapter {

  private final ListAvailableCategoriesUseCase listAvailableCategoriesUseCase;

  private final GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase;

  private final CategoryVersionMapper categoryVersionMapper;

  ListCategoriesResponse listCategories() {

    var availableCategories = listAvailableCategoriesUseCase.getAvailableCategories();
    var categories =
        categoryVersionMapper.mapCategoryListToV1(availableCategories.getCategoriesList());

    return ListCategoriesResponse.newBuilder()
        .addAllCategories(categories)
        .build();
  }

  BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      @Valid BatchGetMatchCategoryValuesRequest request) {

    var matchValuesList =
        categoryVersionMapper.mapStringListToCategoryMatches(request.getMatchValuesList());
    var batchGetCategoryValuesForMatchesResponse =
        getMatchCategoryValuesUseCase.batchGetMatchCategoryValues(matchValuesList);

    return categoryVersionMapper.mapBatchResponse(batchGetCategoryValuesForMatchesResponse);
  }
}
