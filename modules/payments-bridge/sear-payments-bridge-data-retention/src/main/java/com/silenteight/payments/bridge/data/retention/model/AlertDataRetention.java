package com.silenteight.payments.bridge.data.retention.model;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class AlertDataRetention {

  String alertName;
  OffsetDateTime alertTime;

}
