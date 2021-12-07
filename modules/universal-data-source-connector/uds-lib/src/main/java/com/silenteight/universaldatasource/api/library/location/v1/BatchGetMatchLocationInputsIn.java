package com.silenteight.universaldatasource.api.library.location.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.location.v1.BatchGetMatchLocationInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchLocationInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchLocationInputsRequest toBatchGetMatchLocationInputsRequest() {
    return BatchGetMatchLocationInputsRequest
        .newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
