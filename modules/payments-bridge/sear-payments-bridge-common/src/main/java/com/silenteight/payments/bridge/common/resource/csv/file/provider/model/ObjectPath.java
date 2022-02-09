package com.silenteight.payments.bridge.common.resource.csv.file.provider.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ObjectPath {

  String name;

  String bucket;
}
