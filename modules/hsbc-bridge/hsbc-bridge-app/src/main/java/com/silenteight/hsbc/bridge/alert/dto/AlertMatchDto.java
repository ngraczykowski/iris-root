package com.silenteight.hsbc.bridge.alert.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AlertMatchDto {

  String matchId;
  String name;
}
