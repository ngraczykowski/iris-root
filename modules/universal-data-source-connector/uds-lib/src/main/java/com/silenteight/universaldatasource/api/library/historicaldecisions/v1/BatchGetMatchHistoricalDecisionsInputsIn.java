package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.historicaldecisions.v1.BatchGetMatchHistoricalDecisionsInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchHistoricalDecisionsInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchHistoricalDecisionsInputsRequest toBatchGetMatchHistoricalDecisionsInputsRequest() {
    return BatchGetMatchHistoricalDecisionsInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
