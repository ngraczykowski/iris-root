package com.silenteight.hsbc.datasource.common;

import lombok.NonNull;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;

public interface DataSourceInputProvider<R> {

  default R provideInput(@NonNull DataSourceInputRequest request) {
    var matches = getMatchFacade().getMatches(request.getMatchIds());

    return toResponse(
        DataSourceInputCommand.builder()
            .matches(matches)
            .features(request.getFeatures())
            .build());
  }

  R toResponse(DataSourceInputCommand command);

  MatchFacade getMatchFacade();
}
