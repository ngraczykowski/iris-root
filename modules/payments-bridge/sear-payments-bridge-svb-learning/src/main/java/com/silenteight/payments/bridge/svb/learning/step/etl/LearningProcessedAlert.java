package com.silenteight.payments.bridge.svb.learning.step.etl;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LearningProcessedAlert {

  Long jobId;

  String result;

  String errorMessage;

  String fkcoVSystemId;

  String fileName;
}
