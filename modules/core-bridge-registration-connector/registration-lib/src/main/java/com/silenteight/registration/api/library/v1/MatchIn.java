package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.registration.internal.proto.v1.Match;

@Value
@Builder
public class MatchIn {

  String matchId;

  Match toMatch() {
    return Match.newBuilder()
        .setMatchId(matchId)
        .build();
  }
}
