package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SpecificCommonTermsRequest {

  String allMatchFieldsValue;

  Boolean isAccountNumberFlagInMatchingField;
}
