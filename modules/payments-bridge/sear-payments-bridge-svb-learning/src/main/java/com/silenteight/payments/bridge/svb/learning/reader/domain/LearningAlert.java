package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class LearningAlert {

  long alertId;

  List<LearningMatch> matches;
}
