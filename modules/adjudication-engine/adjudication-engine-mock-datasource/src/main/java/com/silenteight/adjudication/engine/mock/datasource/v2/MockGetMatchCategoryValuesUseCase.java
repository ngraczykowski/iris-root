package com.silenteight.adjudication.engine.mock.datasource.v2;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.categories.api.v2.CategoryMatches;
import com.silenteight.datasource.categories.api.v2.CategoryValue;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
class MockGetMatchCategoryValuesUseCase {

  public List<CategoryValue> getMatchCategoryValues(List<CategoryMatches> matchValues) {
    return matchValues.stream()
        .map(mv -> mv
            .getMatchesList()
            .stream()
            .map(m -> getCategoryValue(mv.getCategories(), m))
            .collect(toList()))
        .collect(toList())
        .stream().flatMap(List::stream)
        .collect(toList());
  }

  private static CategoryValue getCategoryValue(String category, String match) {
    var builder = CategoryValue.newBuilder()
        .setName(category)
        .setMatch(match);

    if (category.contains("categories/source_system")) {
      builder.setSingleValue("ECDD");
    } else if (category.contains("categories/country")) {
      builder.setSingleValue("PL");
    } else if (category.contains("categories/customer_type")) {
      builder.setSingleValue("I");
    } else if (category.contains("categories/hit_type")) {
      builder.setSingleValue("DENY");
    } else if (category.contains("categories/segment")) {
      builder.setSingleValue("CONSUMER");
    } else if (category.contains("categories/hit_category")) {
      builder.setSingleValue("DENY");
    } else {
      log.warn("Category is unknown for the mock, returning dummy: match={}", match);
      builder.setSingleValue("UNKNOWN");
    }

    return builder.build();
  }
}
