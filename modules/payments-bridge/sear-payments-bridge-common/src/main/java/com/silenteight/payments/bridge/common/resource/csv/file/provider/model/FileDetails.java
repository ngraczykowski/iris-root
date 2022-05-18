package com.silenteight.payments.bridge.common.resource.csv.file.provider.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public class FileDetails {

  String name;
  String bucket;
  Instant lastModified;
}
