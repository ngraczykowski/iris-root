package com.silenteight.searpayments.scb.mapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DelimiterInNameLineAgentRequest {

  String allMatchingFieldsValue;
  String messageFieldStructureText;
}
