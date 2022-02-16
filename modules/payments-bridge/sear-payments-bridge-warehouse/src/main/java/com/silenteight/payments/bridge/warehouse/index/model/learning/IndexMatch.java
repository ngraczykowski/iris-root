package com.silenteight.payments.bridge.warehouse.index.model.learning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class IndexMatch {
  String matchId;
  String matchName;
  String matchingTexts;
}
