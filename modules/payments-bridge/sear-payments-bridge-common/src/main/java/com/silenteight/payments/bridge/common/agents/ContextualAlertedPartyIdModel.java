package com.silenteight.payments.bridge.common.agents;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ContextualAlertedPartyIdModel {

  String matchingField;

  String matchText;

  int numberOfTokensLeft;

  int numberOfTokensRight;

  int minTokens;

  boolean lineBreaks;
}
