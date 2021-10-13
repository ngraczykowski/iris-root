package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TriggerAlertRequest {

  List<String> alertNames;

  public static TriggerAlertRequest fromAlertNames(List<String> names) {
    return TriggerAlertRequest.builder().alertNames(names).build();
  }
}
