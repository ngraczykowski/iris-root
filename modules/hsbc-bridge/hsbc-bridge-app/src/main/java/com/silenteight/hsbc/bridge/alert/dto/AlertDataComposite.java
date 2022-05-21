package com.silenteight.hsbc.bridge.alert.dto;

import lombok.Value;

@Value
public class AlertDataComposite {

  String bulkId;
  byte[] payload;
}
