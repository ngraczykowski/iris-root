package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.registration.api.v1.RegisteredMatch;

@Value
@Builder
public class RegisteredMatchOut {

  String matchId;
  String matchName;

  static RegisteredMatchOut createFrom(RegisteredMatch input) {
    return RegisteredMatchOut.builder()
        .matchId(input.getMatchId())
        .matchName(input.getMatchName())
        .build();
  }
}
