package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v1.BatchGetMatchCategoryValuesResponse;
import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;
import com.silenteight.payments.bridge.datasource.category.CategoryFacade;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class CategoryService {

  private final CategoryFacade facade;

  ListCategoriesResponse listCategories() {
    return facade.listCategories();
  }

  BatchGetMatchCategoryValuesResponse batchGetMatchCategoryValues(
      BatchGetMatchCategoryValuesRequest request) {
    return facade.batchGetMatchCategoryValues(request.getMatchValuesList());
  }
}
