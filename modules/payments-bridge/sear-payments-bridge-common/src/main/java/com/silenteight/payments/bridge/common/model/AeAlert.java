package com.silenteight.payments.bridge.common.model;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class AeAlert {
  UUID alertId;
  String alertName;
  Map<String, String> matches;
}
