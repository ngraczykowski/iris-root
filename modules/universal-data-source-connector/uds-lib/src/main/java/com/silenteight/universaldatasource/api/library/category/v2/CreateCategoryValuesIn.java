package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class CreateCategoryValuesIn {

  String category;

  @Builder.Default
  List<CategoryValueIn> categoryValues = List.of();

  CreateCategoryValuesRequest toCreateCategoryValuesRequest() {
    return CreateCategoryValuesRequest
        .newBuilder()
        .setCategory(category)
        .addAllCategoryValues(categoryValues.stream()
            .map(CategoryValueIn::toCategoryValue)
            .collect(Collectors.toList())
        )
        .build();
  }
}
