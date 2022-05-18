package com.silenteight.payments.bridge.warehouse.index.model.learning;

import lombok.Value;

@Value
public class IndexAlertIdSet {
  String alertId;
  String alertName;
  String systemId;
  String messageId;

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }
}
