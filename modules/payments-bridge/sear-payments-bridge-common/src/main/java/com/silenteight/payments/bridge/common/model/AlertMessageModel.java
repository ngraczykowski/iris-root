package com.silenteight.payments.bridge.common.model;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class AlertMessageModel {

  UUID id;
  OffsetDateTime receivedAt;
  String dataCenter;
  String unit;
  String businessUnit;
  String messageId;
  String systemId;
  String decisionUrl;
  Integer priority;

}
