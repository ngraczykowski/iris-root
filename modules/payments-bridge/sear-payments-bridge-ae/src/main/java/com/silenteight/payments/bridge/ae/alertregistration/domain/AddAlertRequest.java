package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AddAlertRequest {

  List<String> alertNames;

  public static AddAlertRequest fromAlertNames(List<String> names) {
    return AddAlertRequest.builder().alertNames(names).build();
  }
}
