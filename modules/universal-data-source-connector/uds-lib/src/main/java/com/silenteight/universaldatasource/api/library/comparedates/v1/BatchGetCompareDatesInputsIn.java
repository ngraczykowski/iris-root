package com.silenteight.universaldatasource.api.library.comparedates.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.compareDates.v1.BatchGetCompareDatesInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetCompareDatesInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetCompareDatesInputsRequest toBatchGetCompareDatesInputsRequest() {
    return BatchGetCompareDatesInputsRequest
        .newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
