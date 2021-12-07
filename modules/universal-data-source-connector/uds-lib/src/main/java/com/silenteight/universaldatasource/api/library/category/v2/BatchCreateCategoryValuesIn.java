package com.silenteight.universaldatasource.api.library.category.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class BatchCreateCategoryValuesIn {

  @Builder.Default
  List<CreateCategoryValuesIn> requests = List.of();

  BatchCreateCategoryValuesRequest toBatchCreateCategoryValuesRequest() {
    return BatchCreateCategoryValuesRequest.newBuilder()
        .addAllRequests(requests
            .stream()
            .map(CreateCategoryValuesIn::toCreateCategoryValuesRequest)
            .collect(Collectors.toList())
        )
        .build();
  }
}
