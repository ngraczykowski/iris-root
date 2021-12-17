package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class AlertDetails {

  long alertId;

  long fkcoId;

  public AlertComposite toAlertComposite(
      Map<Long, List<HitComposite>> hits, Map<Long, List<ActionComposite>> actions) {
    return AlertComposite
        .builder()
        .alertId(alertId)
        .fkcoId(fkcoId)
        .hits(hits.get(fkcoId))
        .actions(actions.get(fkcoId))
        .build();
  }
}
