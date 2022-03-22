package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class BatchCreateCategoryValuesOut {

  @Builder.Default
  List<CreatedCategoryValueOut> createdCategoryValues = List.of();

  static BatchCreateCategoryValuesOut createFrom(BatchCreateCategoryValuesResponse response) {
    return BatchCreateCategoryValuesOut.builder()
        .createdCategoryValues(
            response.getCreatedCategoryValuesList()
                .stream()
                .map(CreatedCategoryValueOut::createFrom)
                .collect(Collectors.toList()))
        .build();
  }
}
