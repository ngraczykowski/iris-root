package com.silenteight.universaldatasource.api.library.document.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchDocumentInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchDocumentInputsRequest toBatchGetMatchDocumentInputsRequest() {
    return BatchGetMatchDocumentInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
