package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Builder
@Value
public class AlertIn {

  @NonNull String alertId;
  @NonNull Integer alertPriority;
  OffsetDateTime alertTime;
}
