package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Collection;

@Builder
@Value
public class BatchRegisterAlertMatchesIn {

  @NonNull String alertId;
  @NonNull Integer alertPriority;
  @NonNull Collection<String> matchIds;
  OffsetDateTime alertTime;
}
