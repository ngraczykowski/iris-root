package com.silenteight.adjudication.engine.mock.datasource;

import com.silenteight.datasource.categories.api.v1.CategoryValue;

import java.util.List;
import java.util.stream.Collectors;

class MockGetMatchCategoryValuesUseCase {

  public List<CategoryValue> getMatchCategoryValues(List<String> matchValues) {
    return matchValues.stream()
        .map(MockGetMatchCategoryValuesUseCase::getCategoryValue)
        .collect(Collectors.toList());
  }

  private static CategoryValue getCategoryValue(String matchValue) {
    if (matchValue.contains("categories/source_system")) {
      return CategoryValue.newBuilder()
          .setSingleValue("ECDD")
          .setName(matchValue)
          .build();
    } else if (matchValue.contains("categories/country")) {
      return CategoryValue.newBuilder()
          .setSingleValue("PL")
          .setName(matchValue)
          .build();
    } else if (matchValue.contains("categories/customer_type")) {
      return CategoryValue.newBuilder()
          .setSingleValue("I")
          .setName(matchValue)
          .build();
    } else if (matchValue.contains("categories/hit_type")) {
      return CategoryValue.newBuilder()
          .setSingleValue("DENY")
          .setName(matchValue)
          .build();
    } else if (matchValue.contains("categories/segment")) {
      return CategoryValue.newBuilder()
          .setSingleValue("CONSUMER")
          .setName(matchValue)
          .build();
    } else {
      throw new IllegalArgumentException("Could not find category match in mock");
    }
  }
}
