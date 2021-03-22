package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.dto.gender.GenderInputResponse;

@RequiredArgsConstructor
class GenderInputProvider implements DataSourceInputProvider<GenderInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public GenderInputResponse toResponse(DataSourceInputCommand command) {
    return null;
  }
}
