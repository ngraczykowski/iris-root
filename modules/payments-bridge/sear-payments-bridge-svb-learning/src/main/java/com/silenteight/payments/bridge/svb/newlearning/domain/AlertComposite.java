package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.learning.reader.domain.FindRegisteredAlertRequest;

import java.util.List;

@Value
@Builder
public class AlertComposite {

  AlertDetails alertDetails;

  List<HitComposite> hits;

  List<ActionComposite> actions;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return alertDetails.toFindRegisterAlertRequest();
  }
}
