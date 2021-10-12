package com.silenteight.payments.bridge.svb.etl.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.etl.response.MessageFieldStructure;

import java.util.List;

@Value
@Builder
public class GetAccountNumberRequest {

  String applicationCode;
  String tag;
  String message;
  List<String> matchingFields;
  MessageFieldStructure messageFieldStructure;
}
