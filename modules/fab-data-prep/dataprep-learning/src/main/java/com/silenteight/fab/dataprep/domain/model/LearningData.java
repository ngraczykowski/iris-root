package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class LearningData {

  @NonNull
  String alertName;
  @NonNull
  String originalAnalystDecision;
  @NonNull
  String analystDecision;
  @NonNull
  String analystDecisionModifiedDateTime;
  @NonNull
  String analystReason;
  @NonNull
  String discriminator;
  @NonNull
  String accessPermissionTag;
}
