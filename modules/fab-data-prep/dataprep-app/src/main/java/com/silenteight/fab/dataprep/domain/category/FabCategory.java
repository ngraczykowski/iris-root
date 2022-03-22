package com.silenteight.fab.dataprep.domain.category;

import com.silenteight.fab.dataprep.infrastructure.grpc.CategoriesConfigurationProperties.CategoryDefinition;
import com.silenteight.universaldatasource.api.library.category.v2.CategoryValueIn;

public interface FabCategory {

  String getCategoryName();

  CategoryDefinition getCategoryDefinition();

  CategoryValueIn buildCategory(BuildCategoryCommand buildCategoryCommand);
}
