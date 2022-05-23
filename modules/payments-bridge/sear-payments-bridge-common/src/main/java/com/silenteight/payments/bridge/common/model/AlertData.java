package com.silenteight.payments.bridge.common.model;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class AlertData implements AlertId {

  UUID alertId;
  OffsetDateTime receivedAt;
  String dataCenter;
  String unit;
  String businessUnit;
  String messageId;
  String systemId;
  String decisionUrl;
  Integer priority;
  Integer numberOfHits;
  String userLogin;
  @ToString.Exclude
  String userPassword;

  public String getDiscriminator() {
    return alertId.toString();
  }

}
