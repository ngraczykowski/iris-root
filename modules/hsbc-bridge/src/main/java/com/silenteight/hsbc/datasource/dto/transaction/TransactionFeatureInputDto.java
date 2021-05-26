package com.silenteight.hsbc.datasource.dto.transaction;

import lombok.Builder;
import lombok.Value;

import java.util.List;

import static java.util.Collections.emptyList;

@Builder
@Value
public class TransactionFeatureInputDto {

  String feature;
  @Builder.Default
  List<String> transactionMessages = emptyList();
  // new proto
}
