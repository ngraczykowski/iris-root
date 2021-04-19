package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;
import lombok.Value;


@RequiredArgsConstructor
@Value
public class AdjudicateFailedEvent {

  String bulkId;
  String errorMessage;
}
