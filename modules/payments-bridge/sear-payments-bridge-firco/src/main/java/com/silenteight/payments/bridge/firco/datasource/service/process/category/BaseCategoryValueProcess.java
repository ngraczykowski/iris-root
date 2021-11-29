package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.firco.datasource.model.CategoryValueExtractModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

abstract class BaseCategoryValueProcess implements CategoryValueProcess {

  private static final String CATEGORY_NAME_PREFIX = "categories/";

  @Override
  public CategoryValue extract(CategoryValueExtractModel categoryValueExtractModel) {
    return CategoryValue
        .newBuilder()
        .setName(getFullCategoryName())
        .setAlert(categoryValueExtractModel.getAlertName())
        .setMatch(categoryValueExtractModel.getMatchName())
        .setSingleValue(getValue(categoryValueExtractModel.getHitData()))
        .build();
  }

  private String getFullCategoryName() {
    return CATEGORY_NAME_PREFIX + getCategoryName();
  }

  protected abstract String getCategoryName();

  protected abstract String getValue(HitData hitData);

}
