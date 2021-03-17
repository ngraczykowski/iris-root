package com.silenteight.serp.governance.model.category;

import static java.util.List.of;

public class CategoryFixture {

  public static final String APTYPE_CATEGORY_NAME = "categories/apType";
  public static final CategoryType APTYPE_CATEGORY_TYPE = CategoryType.ENUMERATED;
  public static final String APTYPE_COMPANY_VALUE = "COMPANY";
  public static final String APTYPE_INDIVIDUAL_VALUE = "INDIVIDUAL";

  public static final CategoryDto APTYPE_CATEGORY = CategoryDto.builder()
      .name(APTYPE_CATEGORY_NAME)
      .type(APTYPE_CATEGORY_TYPE)
      .values(of(APTYPE_INDIVIDUAL_VALUE, APTYPE_COMPANY_VALUE))
      .build();
}
