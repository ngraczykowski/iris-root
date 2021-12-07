package com.silenteight.universaldatasource.api.library.transaction.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchTransactionInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchTransactionInputsRequest toBatchGetMatchTransactionInputsRequest() {
    return BatchGetMatchTransactionInputsRequest
        .newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
