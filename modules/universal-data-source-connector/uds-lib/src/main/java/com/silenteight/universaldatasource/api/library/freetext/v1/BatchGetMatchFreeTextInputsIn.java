package com.silenteight.universaldatasource.api.library.freetext.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.freetext.v1.BatchGetMatchFreeTextInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchFreeTextInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchFreeTextInputsRequest toBatchGetMatchFreeTextInputsRequest() {
    return BatchGetMatchFreeTextInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
