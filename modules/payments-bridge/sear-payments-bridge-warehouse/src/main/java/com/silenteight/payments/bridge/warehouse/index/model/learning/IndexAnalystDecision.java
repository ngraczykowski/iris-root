package com.silenteight.payments.bridge.warehouse.index.model.learning;

import lombok.Value;

@Value
public class IndexAnalystDecision {
  String status;
  String decision;
  String comment;
  String actionDateTime;
}
