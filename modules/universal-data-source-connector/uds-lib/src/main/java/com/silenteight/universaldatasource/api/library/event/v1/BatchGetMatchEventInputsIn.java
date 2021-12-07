package com.silenteight.universaldatasource.api.library.event.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchEventInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchEventInputsRequest toBatchGetMatchEventInputsRequest() {
    return BatchGetMatchEventInputsRequest.newBuilder()
        .addAllFeatures(features)
        .addAllMatches(matches)
        .build();
  }
}
