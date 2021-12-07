package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoriesRequest;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class BatchCreateCategoriesIn {

  @Builder.Default
  List<CategoryShared> categories = List.of();

  BatchCreateCategoriesRequest toBatchCreateCategoriesRequest() {
    return BatchCreateCategoriesRequest
        .newBuilder()
        .addAllCategories(
            categories
                .stream()
                .map(CategoryShared::toCategoryProto)
                .collect(Collectors.toList())
        )
        .build();
  }
}
