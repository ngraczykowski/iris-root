package com.silenteight.hsbc.bridge.alert;

class AlertMapper {

  Alert map(com.silenteight.hsbc.bridge.rest.model.input.Alert alert) {
    var caseWithAlert = alert.getSystemInformation().getCasesWithAlertURL();

    //fill alert

    return new Alert(caseWithAlert.get(0).getId(), "alertProto PLACEHOLDER".getBytes());
  }
}
