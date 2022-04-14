package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class SolvingRequest {

  @NonNull
  Map<String, ParsedAlertMessage> extractedAlerts;
}
