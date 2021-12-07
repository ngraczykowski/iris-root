package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder(access = AccessLevel.PACKAGE)
public class BatchCreateCategoriesOut {

  @Builder.Default
  List<CategoryShared> categories = List.of();

  static BatchCreateCategoriesOut createFrom(BatchCreateCategoriesResponse response) {
    return BatchCreateCategoriesOut.builder()
        .categories(response.getCategoriesList().stream()
            .map(CategoryShared::toCategoryShared)
            .collect(Collectors.toList())
        )
        .build();
  }
}
