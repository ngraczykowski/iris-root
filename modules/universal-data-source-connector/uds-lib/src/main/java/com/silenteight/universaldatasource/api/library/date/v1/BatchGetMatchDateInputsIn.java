package com.silenteight.universaldatasource.api.library.date.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.date.v1.BatchGetMatchDateInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchDateInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchDateInputsRequest toBatchGetMatchDateInputsRequest() {
    return BatchGetMatchDateInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
