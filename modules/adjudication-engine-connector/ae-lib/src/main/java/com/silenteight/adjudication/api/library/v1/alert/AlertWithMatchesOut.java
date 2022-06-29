package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class AlertWithMatchesOut {

  String alertId;
  String alertName;
  OffsetDateTime alertTime;

  @Default
  Map<String, String> labels = Collections.emptyMap();

  @Builder.Default
  List<AlertMatchOut> matches = Collections.emptyList();
}
