package com.silenteight.universaldatasource.api.library.name.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.name.v1.BatchGetMatchNameInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchNameInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchNameInputsRequest toBatchGetMatchNameInputsRequest() {
    return BatchGetMatchNameInputsRequest
        .newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
