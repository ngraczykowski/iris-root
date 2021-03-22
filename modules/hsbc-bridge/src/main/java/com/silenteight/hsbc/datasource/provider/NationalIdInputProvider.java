package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdInputResponse;

@RequiredArgsConstructor
class NationalIdInputProvider implements DataSourceInputProvider<NationalIdInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public NationalIdInputResponse toResponse(DataSourceInputCommand command) {
    return null;
  }
}
