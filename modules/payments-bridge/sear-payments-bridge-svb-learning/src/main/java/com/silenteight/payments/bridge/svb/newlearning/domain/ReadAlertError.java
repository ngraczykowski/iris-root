package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Builder
@Value
@RequiredArgsConstructor
public class ReadAlertError {

  String alertId;
  String exceptionMessage;
}
