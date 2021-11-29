package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

abstract class BaseCategoryValueProcess implements CategoryValueProcess {

  private static final String CATEGORY_NAME_PREFIX = "categories/";

  @Override
  public CategoryValue extract(HitData hitData, String matchName) {
    return CategoryValue
        .newBuilder()
        .setName(getFullCategoryName())
        .setMatch(matchName)
        .setSingleValue(getValue(hitData))
        .build();
  }

  private String getFullCategoryName() {
    return CATEGORY_NAME_PREFIX + getCategoryName();
  }

  protected abstract String getCategoryName();

  protected abstract String getValue(HitData hitData);

}
