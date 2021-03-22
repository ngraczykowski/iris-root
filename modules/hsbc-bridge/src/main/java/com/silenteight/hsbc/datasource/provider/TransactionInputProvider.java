package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionInputResponse;

@RequiredArgsConstructor
class TransactionInputProvider implements DataSourceInputProvider<TransactionInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public TransactionInputResponse toResponse(DataSourceInputCommand command) {
    return TransactionInputResponse.builder().build();
  }
}
