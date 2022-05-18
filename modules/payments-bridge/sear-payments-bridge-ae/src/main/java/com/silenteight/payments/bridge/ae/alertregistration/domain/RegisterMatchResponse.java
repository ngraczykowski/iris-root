package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisterMatchResponse {

  String matchId;

  String matchName;

  public SaveRegisteredMatchRequest toSaveRegisteredMatchRequest() {
    return SaveRegisteredMatchRequest.builder().matchId(matchId).matchName(matchName).build();
  }
}
