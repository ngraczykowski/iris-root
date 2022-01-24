package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LearningProcessedAlert {

  Long jobId;

  String result;

  String errorMessage;

  Long learningProcessedAlertId;

  String fkcoVSystemId;

  String fileName;
}
