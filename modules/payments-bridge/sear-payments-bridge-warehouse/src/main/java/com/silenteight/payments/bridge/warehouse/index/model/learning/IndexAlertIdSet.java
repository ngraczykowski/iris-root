package com.silenteight.payments.bridge.warehouse.index.model.learning;

import lombok.Value;

@Value
public class IndexAlertIdSet {
  String alertMessageId;
  String alertName;
  String systemId;
  String messageId;

  public String getDiscriminator() {
    return alertMessageId;
  }
}
