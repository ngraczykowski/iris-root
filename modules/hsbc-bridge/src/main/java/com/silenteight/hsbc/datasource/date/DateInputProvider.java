package com.silenteight.hsbc.datasource.date;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.match.MatchFacade;
import com.silenteight.hsbc.datasource.date.dto.DateInputRequest;
import com.silenteight.hsbc.datasource.date.dto.DateInputResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DateInputProvider {

  private final MatchFacade matchFacade;

  public DateInputResponse provideInput(@NonNull DateInputRequest request) {
    //TODO awojcicki fill
    var matchIds = request.getMatches().stream().collect(Collectors.toList());
    var matches = matchFacade.getMatches(matchIds);

    // action point
    // check features
    // prepare response by creating data transform

    return DateInputResponse.builder()
        .dateInputs(List.of()) //list of inputs
        .build();
  }
}
