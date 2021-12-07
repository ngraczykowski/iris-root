package com.silenteight.universaldatasource.api.library.ispep.v2;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.ispep.v2.BatchGetMatchIsPepInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchIsPepInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchIsPepInputsRequest toBatchGetMatchIsPepInputsRequest() {
    return BatchGetMatchIsPepInputsRequest
        .newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
