package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchtextFirstTokenOfAddressAgentRequest {

  List<String> matchingTexts;
  List<String> addresses;
}
