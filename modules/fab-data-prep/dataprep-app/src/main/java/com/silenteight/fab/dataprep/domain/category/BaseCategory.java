package com.silenteight.fab.dataprep.domain.category;

import com.silenteight.universaldatasource.api.library.category.v2.CategoryValueIn;

abstract class BaseCategory implements FabCategory {

  public CategoryValueIn buildCategory(
      BuildCategoryCommand buildCategoryCommand) {
    return CategoryValueIn.builder()
        .alert(buildCategoryCommand.getAlertName())
        .match(buildCategoryCommand.getMatch().getMatchName())
        .singleValue(getValue(buildCategoryCommand))
        .build();
  }

  abstract String getValue(BuildCategoryCommand buildCategoryCommand);
}
