package com.silenteight.payments.bridge.firco.datasource.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

@Value
@Builder
public class AlertData {
  // Original alert message
  AlertMessageModel alertMessage;

  ObjectNode alertPayload;

  // alerts/1234
  String aeAlertName;

  // AS01231451 -> alerts/1234/matches/5678
  // GN0987654 -> alerts/1234/matches/9876
  Map<String, String> matchNames;
}
