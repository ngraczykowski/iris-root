package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AlertComposite {

  long alertId;

  long fkcoId;
}
