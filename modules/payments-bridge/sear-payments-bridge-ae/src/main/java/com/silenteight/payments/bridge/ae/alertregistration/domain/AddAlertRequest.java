package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Value;

@Value
public class AddAlertRequest {

  String analysisName;

  String alertName;
}
