package com.silenteight.hsbc.bridge.alert.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlertDto {

  String alertId;
  String name;
}
