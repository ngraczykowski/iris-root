package com.silenteight.hsbc.bridge.recommendation.event;

import lombok.Value;

import java.util.List;

@Value
public class AlertRecommendationsStoredEvent {

  String analysis;
  List<String> alerts;
}
