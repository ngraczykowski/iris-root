package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AlertComposite {

  long alertId;

  long fkcoId;

  List<HitComposite> hits;

  List<ActionComposite> actionComposites;
}
