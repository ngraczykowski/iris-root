package com.silenteight.universaldatasource.app.category.port.incoming;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesResponse;
import com.silenteight.datasource.categories.api.v2.Category;

import java.util.List;

public interface CreateCategoriesUseCase {

  BatchCreateCategoriesResponse createCategories(List<Category> categoriesList);
}
