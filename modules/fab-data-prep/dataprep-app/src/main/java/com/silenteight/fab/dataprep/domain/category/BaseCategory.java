package com.silenteight.fab.dataprep.domain.category;

import com.silenteight.universaldatasource.api.library.category.v2.CategoryValueIn;

import java.util.UUID;

abstract class BaseCategory implements FabCategory {

  public CategoryValueIn buildCategory(
      BuildCategoryCommand buildCategoryCommand) {
    return CategoryValueIn.builder()
        .name(generateValueName())
        .match(buildCategoryCommand.getMatch().getMatchName())
        .singleValue(getValue(buildCategoryCommand))
        .build();
  }

  private String generateValueName() {
    return getCategoryName() + "/values/" + UUID.randomUUID();
  }

  abstract String getValue(BuildCategoryCommand buildCategoryCommand);
}
