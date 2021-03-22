package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.DataSourceInputCommand;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.dto.event.EventInputResponse;

@RequiredArgsConstructor
class EventInputProvider implements DataSourceInputProvider<EventInputResponse> {

  @Getter
  private final MatchFacade matchFacade;

  @Override
  public EventInputResponse toResponse(
      DataSourceInputCommand command) {
    return null;
  }
}
