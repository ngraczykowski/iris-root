package com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.isofgivendocumenttype.v1.BatchGetIsOfGivenDocumentTypeInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetIsOfGivenDocumentTypeInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetIsOfGivenDocumentTypeInputsRequest toBatchGetIsOfGivenDocumentTypeInputsRequest() {
    return BatchGetIsOfGivenDocumentTypeInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
