package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

public interface AvailableCategoriesProvider {

  ListCategoriesResponse getAvailableCategories();

}
