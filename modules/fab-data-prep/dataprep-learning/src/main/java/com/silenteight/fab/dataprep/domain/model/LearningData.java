package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class LearningData {

  @NonNull
  String alertName;
  String analystDecision;
  String analystDecisionModifiedDateTime;
  String analystReason;
  String systemId;
  String messageId;
}
