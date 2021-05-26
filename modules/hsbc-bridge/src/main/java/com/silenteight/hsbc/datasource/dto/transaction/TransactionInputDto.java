package com.silenteight.hsbc.datasource.dto.transaction;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class TransactionInputDto {

  String match;
  @Builder.Default
  List<TransactionFeatureInputDto> featureInputs = emptyList();
}
