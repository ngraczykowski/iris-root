package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.learning.reader.domain.FindRegisteredAlertRequest;

import java.time.OffsetDateTime;

@Value
@Builder
public class AlertDetails {

  private static final int LEARNING_PRIORITY = 3;

  long alertId;

  long fkcoId;

  OffsetDateTime fkcoDFilteredDateTime;

  String systemId;

  String messageId;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return FindRegisteredAlertRequest.builder().messageId(messageId).systemId(systemId).build();
  }
}
