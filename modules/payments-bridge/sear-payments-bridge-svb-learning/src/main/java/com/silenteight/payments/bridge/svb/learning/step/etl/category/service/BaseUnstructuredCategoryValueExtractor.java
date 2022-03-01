package com.silenteight.payments.bridge.svb.learning.step.etl.category.service;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

abstract class BaseUnstructuredCategoryValueExtractor
    implements UnstructuredCategoryValueExtractor {

  private static final String CATEGORY_NAME_PREFIX = "categories/";

  @Override
  public CategoryValue extract(HitComposite hit, RegisterAlertResponse registeredAlert) {
    return CategoryValue
        .newBuilder()
        .setName(getFullCategoryName())
        .setAlert(registeredAlert.getAlertName())
        .setMatch(registeredAlert.getMatchName(hit.getMatchId()))
        .setSingleValue(getValue(hit))
        .build();
  }

  private String getFullCategoryName() {
    return CATEGORY_NAME_PREFIX + getCategoryName();
  }

  protected abstract String getCategoryName();

  protected abstract String getValue(HitComposite hitComposite);
}
