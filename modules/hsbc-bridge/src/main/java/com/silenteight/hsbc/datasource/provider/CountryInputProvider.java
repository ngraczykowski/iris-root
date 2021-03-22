package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.dto.country.CountryInputResponse;

@RequiredArgsConstructor
class CountryInputProvider implements DataSourceInputProvider<CountryInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public CountryInputResponse toResponse(
      DataSourceInputCommand command) {
    return null;
  }
}
