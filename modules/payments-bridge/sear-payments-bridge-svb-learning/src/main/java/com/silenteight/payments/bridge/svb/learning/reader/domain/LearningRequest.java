package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Getter;
import lombok.Value;

import javax.annotation.Nullable;

@Value
public class LearningRequest {

  String bucket;

  String object;

  @Getter(onMethod_ = @Nullable)
  String region;
}
