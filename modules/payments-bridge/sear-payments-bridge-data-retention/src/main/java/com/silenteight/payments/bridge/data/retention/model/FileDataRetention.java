package com.silenteight.payments.bridge.data.retention.model;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class FileDataRetention {

  String fileName;
}
