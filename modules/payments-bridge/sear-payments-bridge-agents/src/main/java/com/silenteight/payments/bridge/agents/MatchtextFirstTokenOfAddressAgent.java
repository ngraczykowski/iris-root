package com.silenteight.payments.bridge.agents;

import lombok.NonNull;

public class MatchtextFirstTokenOfAddressAgent {

  @NonNull
  public MatchtextFirstTokenOfAddressAgentResponse invoke(
      @NonNull MatchtextFirstTokenOfAddressAgentRequest
          matchtextFirstTokenOfAddressAgentRequest) {

    if (MatchtextFirstTokenOfAddressAgentHelper.isMatchtextFirstTokenOfAddress(
        matchtextFirstTokenOfAddressAgentRequest.getMatchingTexts(),
        matchtextFirstTokenOfAddressAgentRequest.getAddresses())) {
      return MatchtextFirstTokenOfAddressAgentResponse.YES;
    } else {
      return MatchtextFirstTokenOfAddressAgentResponse.NO;
    }
  }
}
