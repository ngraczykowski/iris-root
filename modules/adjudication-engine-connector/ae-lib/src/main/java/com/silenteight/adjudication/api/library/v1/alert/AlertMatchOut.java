package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlertMatchOut {

  String matchId;
  String name;
}
