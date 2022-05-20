package com.silenteight.hsbc.datasource.dto.transaction;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class TransactionInputDto {

  String match;
  @Builder.Default
  List<TransactionFeatureInputDto> featureInputs = Collections.emptyList();
}
