package com.silenteight.connector.ftcc.callback.outgoing;

import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@ToString
public class RecommendationsDeliveredEvent {

  @NonNull
  String analysisName;
  @NonNull
  String batchName;
  @NonNull
  String alertType;
}
