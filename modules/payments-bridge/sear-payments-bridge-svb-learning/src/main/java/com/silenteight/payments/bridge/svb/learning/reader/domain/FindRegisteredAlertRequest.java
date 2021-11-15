package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Value;

@Value
public class FindRegisteredAlertRequest {

  String systemId;
  String messageId;
}
