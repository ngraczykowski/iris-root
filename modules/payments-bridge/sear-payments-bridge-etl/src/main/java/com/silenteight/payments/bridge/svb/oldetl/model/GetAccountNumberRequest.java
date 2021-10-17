package com.silenteight.payments.bridge.svb.oldetl.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GetAccountNumberRequest {

  String tag;
  List<String> matchingFields;
}
