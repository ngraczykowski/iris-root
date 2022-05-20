package com.silenteight.universaldatasource.app.category.port.incoming;


import com.silenteight.datasource.categories.api.v2.ListCategoriesResponse;

public interface ListAvailableCategoriesUseCase {

  ListCategoriesResponse getAvailableCategories();

}
