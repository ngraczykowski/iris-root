package com.silenteight.fab.dataprep.domain.category;

import com.silenteight.universaldatasource.api.library.category.v2.CategoryValueIn;

public interface FabCategory {

  String getCategoryName();

  CategoryValueIn buildCategory(BuildCategoryCommand buildCategoryCommand);
}
