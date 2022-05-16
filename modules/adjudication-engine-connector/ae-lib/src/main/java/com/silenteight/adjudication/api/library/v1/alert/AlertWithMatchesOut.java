package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Value
@Builder
public class AlertWithMatchesOut {

  String alertId;
  String alertName;
  OffsetDateTime alertTime;

  @Builder.Default
  List<AlertMatchOut> matches = Collections.emptyList();
}
