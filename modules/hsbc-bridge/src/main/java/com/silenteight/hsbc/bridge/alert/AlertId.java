package com.silenteight.hsbc.bridge.alert;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlertId {

  long id;
  String externalId;
}
