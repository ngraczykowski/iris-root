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

  String systemId;

  String messageId;

  public AlertComposite toAlertComposite(
      Map<Long, List<HitComposite>> hits, Map<Long, List<ActionComposite>> actions) {
    return AlertComposite
        .builder()
        .alertId(alertId)
        .fkcoId(fkcoId)
        .systemId(systemId)
        .messageId(messageId)
        .hits(hits.get(fkcoId))
        .actions(actions.get(fkcoId))
        .build();
  }
}
