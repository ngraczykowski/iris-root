package com.silenteight.payments.bridge.svb.newlearning.step.etl.category.service;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

abstract class BaseCategoryValueExtractor implements CategoryValueExtractor {

  private static final String CATEGORY_NAME_PREFIX = "categories/";

  @Override
  public CategoryValue extract(EtlHit etlHit, RegisterAlertResponse registeredAlert) {
    return CategoryValue
        .newBuilder()
        .setName(getFullCategoryName())
        .setAlert(registeredAlert.getAlertName())
        .setMatch(registeredAlert.getMatchName(etlHit.getMatchId()))
        .setSingleValue(getValue(etlHit))
        .build();
  }

  private String getFullCategoryName() {
    return CATEGORY_NAME_PREFIX + getCategoryName();
  }

  protected abstract String getCategoryName();

  protected abstract String getValue(EtlHit etlHit);
}
