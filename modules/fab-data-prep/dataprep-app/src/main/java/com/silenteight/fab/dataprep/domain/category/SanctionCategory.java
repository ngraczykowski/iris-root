package com.silenteight.fab.dataprep.domain.category;

import lombok.RequiredArgsConstructor;

import com.silenteight.universaldatasource.api.library.category.v2.CategoryValueIn;

import java.util.UUID;

@RequiredArgsConstructor
public class SanctionCategory implements FabCategory {

  private static final String SANCTION_TEXT = "SAN!";

  private final String categoryName;

  @Override
  public String getCategoryName() {
    return "categories/" + categoryName;
  }

  @Override
  public CategoryValueIn buildCategory(
      BuildCategoryCommand buildCategoryCommand) {
    return CategoryValueIn.builder()
        .name(generateValueName())
        .match(buildCategoryCommand.getMatchName())
        .singleValue(getValue(buildCategoryCommand.getSystemId()))
        .build();
  }

  private String generateValueName() {
    return getCategoryName() + "/values/" + UUID.randomUUID();
  }

  private static String getValue(String systemId) {
    return systemId.contains(SANCTION_TEXT) ? "True" : "False";
  }
}
