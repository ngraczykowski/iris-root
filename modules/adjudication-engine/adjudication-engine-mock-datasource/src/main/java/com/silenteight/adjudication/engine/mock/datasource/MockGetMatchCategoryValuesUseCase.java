package com.silenteight.adjudication.engine.mock.datasource;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v1.CategoryValue;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class MockGetMatchCategoryValuesUseCase {

  public List<CategoryValue> getMatchCategoryValues(List<String> matchValues) {
    return matchValues.stream()
        .map(MockGetMatchCategoryValuesUseCase::getCategoryValue)
        .collect(Collectors.toList());
  }

  private static CategoryValue getCategoryValue(String matchValue) {
    var builder = CategoryValue.newBuilder()
        .setName(matchValue);

    if (matchValue.contains("categories/source_system")) {
      builder.setSingleValue("ECDD");
    } else if (matchValue.contains("categories/country")) {
      builder.setSingleValue("PL");
    } else if (matchValue.contains("categories/customer_type")) {
      builder.setSingleValue("I");
    } else if (matchValue.contains("categories/hit_type")) {
      builder.setSingleValue("DENY");
    } else if (matchValue.contains("categories/segment")) {
      builder.setSingleValue("CONSUMER");
    } else {
      log.warn("Category is unknown for the mock, returning dummy: match={}", matchValue);
      builder.setSingleValue("UNKNOWN");
    }

    return builder.build();
  }
}
