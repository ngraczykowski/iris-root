package com.silenteight.hsbc.datasource.dto.transaction;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class TransactionFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> transactionMessages = Collections.emptyList();
  // new proto
}
