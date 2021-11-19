package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ObjectPath {

  String name;

  String bucket;
}
