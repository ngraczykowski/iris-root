package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Value;

@Value
public class LearningRequest {

  String bucket;

  String object;
}
