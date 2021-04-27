package com.silenteight.hsbc.datasource.provider;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputRequest;
import com.silenteight.hsbc.datasource.dto.ispep.IsPepInputResponse;

import java.util.List;

@RequiredArgsConstructor
public class IsPepInputProvider {

  @Getter
  private final MatchFacade matchFacade;

  public IsPepInputResponse provideInput(@NonNull IsPepInputRequest request) {

    return IsPepInputResponse.builder()
        .inputs(List.of()) //TODO
        .build();
  }
}
