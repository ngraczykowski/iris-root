package com.silenteight.adjudication.engine.mock.datasource.v2;

import com.silenteight.datasource.categories.api.v1.Category;
import com.silenteight.datasource.categories.api.v1.CategoryType;

import java.util.List;

class MockListCategoriesUseCase {

  public List<Category> findAllCategories() {
    return List.of(
        Category.newBuilder().setName("country")
            .setType(CategoryType.ANY_STRING)
            .setDisplayName("Country")
            .setMultiValue(true)
            .build(),
        Category.newBuilder().setName("customerType")
            .setDisplayName("Customer Type (Individual / Company)")
            .setType(CategoryType.ENUMERATED)
            .setMultiValue(false)
            .addAllAllowedValues(List.of("I", "C"))
            .build(),
        Category.newBuilder().setName("hitType")
            .setDisplayName("Hit Type (PEP / AM / SAN)")
            .setType(CategoryType.ENUMERATED)
            .setMultiValue(false)
            .addAllAllowedValues(List.of("SAN", "PEP", "AM"))
            .build());
  }
}
