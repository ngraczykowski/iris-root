package com.silenteight.payments.bridge.datasource.category;

import com.silenteight.datasource.categories.api.v1.Category;

import java.util.List;

public interface AvailableCategoriesProvider {

  List<Category> getAvailableCategories();
}
