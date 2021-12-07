package com.silenteight.universaldatasource.api.library.gender.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.gender.v1.BatchGetMatchGenderInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchGenderInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchGenderInputsRequest toBatchGetMatchGenderInputsRequest() {
    return BatchGetMatchGenderInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
