package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SpecificTermsRequest {

  String allMatchFieldsValue;
}
