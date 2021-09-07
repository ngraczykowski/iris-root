package com.silenteight.payments.bridge.datasource.category.port.incoming;

import com.silenteight.datasource.categories.api.v1.ListCategoriesResponse;

public interface ListAvailableCategoriesUseCase {

  ListCategoriesResponse getAvailableCategories();

}
