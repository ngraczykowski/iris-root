package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SaveRegisteredMatchRequest {

  String matchName;

  String matchId;
}
