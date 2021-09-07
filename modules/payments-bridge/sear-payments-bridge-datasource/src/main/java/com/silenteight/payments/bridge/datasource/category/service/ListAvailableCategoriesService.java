package com.silenteight.payments.bridge.datasource.category.service;

import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;
import com.silenteight.payments.bridge.datasource.category.port.incoming.ListAvailableCategoriesUseCase;

import org.springframework.stereotype.Service;

@Service
class ListAvailableCategoriesService implements ListAvailableCategoriesUseCase {

  @Override
  public ListCategoriesResponse getAvailableCategories() {
    // TODO(jgajewski): Implement getAvailableCategories().
    return ListCategoriesResponse.getDefaultInstance();
  }
}
