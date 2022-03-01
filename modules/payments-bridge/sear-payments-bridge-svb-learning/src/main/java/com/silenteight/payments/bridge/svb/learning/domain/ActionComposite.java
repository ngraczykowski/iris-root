package com.silenteight.payments.bridge.svb.learning.domain;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class ActionComposite {

  long actionId;
  OffsetDateTime actionDatetime;
  String statusName;
  String statusBehaviour;
  String actionComment;
}
