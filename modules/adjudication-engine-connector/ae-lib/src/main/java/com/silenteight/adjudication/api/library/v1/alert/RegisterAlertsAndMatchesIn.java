package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@Builder
public class RegisterAlertsAndMatchesIn {

  @Builder.Default
  List<BatchCreateAlertMatchesIn> alertsWithMatches = Collections.emptyList();
}
