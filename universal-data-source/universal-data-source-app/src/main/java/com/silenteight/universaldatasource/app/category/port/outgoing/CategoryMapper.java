package com.silenteight.universaldatasource.app.category.port.outgoing;

import com.silenteight.datasource.categories.api.v2.Category;
import com.silenteight.universaldatasource.app.category.model.CategoryDto;

import java.util.List;

public interface CategoryMapper {

  List<CategoryDto> mapCategories(List<Category> categories);
}
