package com.silenteight.universaldatasource.app.category.port.outgoing;

import com.silenteight.datasource.categories.api.v2.Category;

import java.util.List;

public interface CategoryDataAccess {

  List<Category> getAllCategories();

  List<Category> saveAll(List<Category> categoriesList);
}
