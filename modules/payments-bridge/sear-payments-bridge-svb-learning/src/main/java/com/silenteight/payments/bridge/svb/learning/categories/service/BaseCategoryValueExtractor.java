package com.silenteight.payments.bridge.svb.learning.categories.service;

import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

abstract class BaseCategoryValueExtractor implements CategoryValueExtractor {

  private static final String CATEGORY_NAME_PREFIX = "categories/";

  @Override
  public CategoryValue extract(LearningMatch learningMatch) {
    return CategoryValue
        .newBuilder()
        .setName(getFullCategoryName())
        .setMatch(learningMatch.getMatchName())
        .setSingleValue(getValue(learningMatch))
        .build();
  }

  private String getFullCategoryName() {
    return CATEGORY_NAME_PREFIX + getCategoryName();
  }

  protected abstract String getCategoryName();

  protected abstract String getValue(LearningMatch learningMatch);
}
