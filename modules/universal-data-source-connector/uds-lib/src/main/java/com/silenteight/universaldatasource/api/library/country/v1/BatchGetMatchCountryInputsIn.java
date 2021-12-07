package com.silenteight.universaldatasource.api.library.country.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsRequest;

import java.util.List;

@Value
@Builder
public class BatchGetMatchCountryInputsIn {

  @Builder.Default
  List<String> matches = List.of();

  @Builder.Default
  List<String> features = List.of();

  BatchGetMatchCountryInputsRequest toBatchGetMatchCountryInputsRequest() {
    return BatchGetMatchCountryInputsRequest.newBuilder()
        .addAllMatches(matches)
        .addAllFeatures(features)
        .build();
  }
}
