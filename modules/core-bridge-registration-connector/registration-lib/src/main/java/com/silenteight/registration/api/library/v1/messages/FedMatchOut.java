package com.silenteight.registration.api.library.v1.messages;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FedMatchOut {
  String matchId;
}
