package com.silenteight.payments.bridge.svb.learning.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RegisteredMatch {

  String matchName;

  HitComposite hitComposite;
}
