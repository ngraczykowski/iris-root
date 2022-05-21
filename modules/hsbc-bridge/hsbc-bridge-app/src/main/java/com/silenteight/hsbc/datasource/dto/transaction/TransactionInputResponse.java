package com.silenteight.hsbc.datasource.dto.transaction;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Builder
@Value
public class TransactionInputResponse {

  @Builder.Default
  List<TransactionInputDto> inputs = Collections.emptyList();
}
