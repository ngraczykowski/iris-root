package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Builder
@Value
public class BatchRegisterAlertMatchesIn {

  @NonNull String alertId;
  @NonNull Integer alertPriority;
  @NonNull Collection<String> matchIds;

  @Default
  Map<String, String> labels = Collections.emptyMap();
  OffsetDateTime alertTime;
}
