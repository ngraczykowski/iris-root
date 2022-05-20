package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DelimiterInNameLineAgentRequest {

  String allMatchingFieldsValue;
  String messageFieldStructureText;
}
