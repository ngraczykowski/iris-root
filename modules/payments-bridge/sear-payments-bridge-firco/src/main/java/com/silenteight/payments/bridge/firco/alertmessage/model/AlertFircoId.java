package com.silenteight.payments.bridge.firco.alertmessage.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AlertFircoId {

  String messageId;
  String systemId;

}
