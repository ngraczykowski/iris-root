package com.silenteight.adjudication.engine.mock.datasource;

import com.silenteight.datasource.categories.api.v1.CategoryValue;
import com.silenteight.datasource.categories.api.v1.MultiValue;

import java.util.List;
import java.util.stream.Collectors;

class MockGetMatchCategoryValuesUseCase {

  public List<CategoryValue> getMatchCategoryValues(List<String> matchValues) {
    return matchValues.stream().map(name ->
        CategoryValue.newBuilder()
            .setMultiValue(
                MultiValue.newBuilder().addAllValues(List.of("PL", "SG")))
            .setName(name)
            .build()
    ).collect(Collectors.toList());
  }
}
