package com.silenteight.universaldatasource.api.library.bankidentificationcodes.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.bankidentificationcodes.v1.BatchGetMatchBankIdentificationCodesInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchBankIdentificationCodesInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchBankIdentificationCodesInputsRequest
      toBatchGetMatchBankIdentificationCodesInputsRequest() {
    return BatchGetMatchBankIdentificationCodesInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
