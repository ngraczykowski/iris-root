package com.silenteight.payments.bridge.datasource.category.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;
import com.silenteight.payments.bridge.datasource.category.port.incoming.GetMatchCategoryValuesUseCase;
import com.silenteight.payments.bridge.datasource.category.port.incoming.ListAvailableCategoriesUseCase;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class CategoryAdapter {

  private final ListAvailableCategoriesUseCase listAvailableCategoriesUseCase;
  private final GetMatchCategoryValuesUseCase getMatchCategoryValuesUseCase;

  ListCategoriesResponse listCategories() {
    return listAvailableCategoriesUseCase.getAvailableCategories();
  }

  BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request) {
    return getMatchCategoryValuesUseCase.batchGetMatchCategoryValues(request.getMatchValuesList());
  }
}
