package com.silenteight.payments.bridge.firco.alertmessage.model;

import lombok.Value;

import java.util.UUID;

@Value
public class AlertIdSet {

  UUID alertId;
  String messageId;
  String systemId;

}
