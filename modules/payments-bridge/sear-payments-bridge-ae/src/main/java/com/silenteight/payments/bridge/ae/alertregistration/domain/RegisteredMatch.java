package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisteredMatch {

  String matchName;

  String matchId;
}
