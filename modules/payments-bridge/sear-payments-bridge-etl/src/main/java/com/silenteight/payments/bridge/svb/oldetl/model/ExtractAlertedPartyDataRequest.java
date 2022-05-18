package com.silenteight.payments.bridge.svb.oldetl.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExtractAlertedPartyDataRequest {
  String applicationCode;
  String messageData;
  String messageType;
  String tag;
  String matchingText;
}
