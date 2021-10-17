package com.silenteight.payments.bridge.svb.oldetl.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExtractFieldStructureValue {

  String sourceSystem;
  String tag;
  String messageData;
  boolean isScstar;

}
