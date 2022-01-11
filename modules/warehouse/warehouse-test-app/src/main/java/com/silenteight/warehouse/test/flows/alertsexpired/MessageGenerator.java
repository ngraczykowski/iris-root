package com.silenteight.warehouse.test.flows.alertsexpired;

import lombok.RequiredArgsConstructor;

import com.silenteight.dataretention.api.v1.AlertsExpired;

import java.util.List;

@RequiredArgsConstructor
class MessageGenerator {

  AlertsExpired generateAlertsExpired(List<String> alertNames) {
    return AlertsExpired
        .newBuilder()
        .addAllAlerts(alertNames)
        .build();
  }
}
