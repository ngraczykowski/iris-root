package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FindRegisteredAlertRequest {

  String systemId;
  String messageId;
}
