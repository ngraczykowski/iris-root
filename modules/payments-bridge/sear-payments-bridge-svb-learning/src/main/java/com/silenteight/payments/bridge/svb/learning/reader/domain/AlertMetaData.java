package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AlertMetaData {

  String fileName;

  String batchStamp;
}
