package com.silenteight.hsbc.datasource.dto.transaction;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class TransactionInputResponse {

  List<TransactionInputDto> inputs;
}
