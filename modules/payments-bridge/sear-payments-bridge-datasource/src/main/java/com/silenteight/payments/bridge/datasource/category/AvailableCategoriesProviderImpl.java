package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

import org.springframework.stereotype.Service;

@Service
public class AvailableCategoriesProviderImpl implements AvailableCategoriesProvider {

  @Override
  public ListCategoriesResponse getAvailableCategories() {
    return ListCategoriesResponse.getDefaultInstance();
  }
}
