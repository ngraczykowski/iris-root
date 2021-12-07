package com.silenteight.universaldatasource.api.library.allowlist.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchAllowListInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchAllowListInputsRequest toBatchGetMatchAllowListInputsRequest() {
    return BatchGetMatchAllowListInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
