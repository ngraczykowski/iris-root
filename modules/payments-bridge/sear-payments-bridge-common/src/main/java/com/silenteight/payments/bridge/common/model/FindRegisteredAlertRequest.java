package com.silenteight.payments.bridge.common.model;

import lombok.Value;

@Value
public class FindRegisteredAlertRequest {

  String systemId;
  String messageId;
}
