package com.silenteight.universaldatasource.app.category.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;
import com.silenteight.universaldatasource.app.category.port.incoming.ListAvailableCategoriesUseCase;
import com.silenteight.universaldatasource.app.category.port.outgoing.CategoryDataAccess;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class ListAvailableCategoriesService implements ListAvailableCategoriesUseCase {

  private final CategoryDataAccess categoryDataAccess;

  @Override
  public ListCategoriesResponse getAvailableCategories() {
    return ListCategoriesResponse.newBuilder()
        .addAllCategories(categoryDataAccess.getAllCategories())
        .build();
  }
}
