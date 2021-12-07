package com.silenteight.universaldatasource.api.library.nationalid.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchNationalIdInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchNationalIdInputsRequest toBatchGetMatchNationalIdInputsRequest() {
    return BatchGetMatchNationalIdInputsRequest
        .newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
