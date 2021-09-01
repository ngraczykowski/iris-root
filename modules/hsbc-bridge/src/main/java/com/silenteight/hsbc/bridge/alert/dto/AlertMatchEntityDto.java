package com.silenteight.hsbc.bridge.alert.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AlertMatchEntityDto {

  String name;
  String externalId;
}
