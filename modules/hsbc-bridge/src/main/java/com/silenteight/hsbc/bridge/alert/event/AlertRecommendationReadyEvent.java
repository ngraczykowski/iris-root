package com.silenteight.hsbc.bridge.alert.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AlertRecommendationReadyEvent {

  private final String alertName;
}
