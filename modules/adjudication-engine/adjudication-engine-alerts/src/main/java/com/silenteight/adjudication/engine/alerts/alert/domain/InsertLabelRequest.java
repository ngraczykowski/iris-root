package com.silenteight.adjudication.engine.alerts.alert.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InsertLabelRequest {

  Long alertId;

  String labelName;

  String labelValue;
}
