package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.learning.reader.domain.FindRegisteredAlertRequest;

import java.util.List;

@Value
@Builder
public class AlertComposite {

  long alertId;

  long fkcoId;

  String systemId;

  String messageId;

  List<HitComposite> hits;

  List<ActionComposite> actions;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return FindRegisteredAlertRequest.builder().messageId(messageId).systemId(systemId).build();
  }
}
