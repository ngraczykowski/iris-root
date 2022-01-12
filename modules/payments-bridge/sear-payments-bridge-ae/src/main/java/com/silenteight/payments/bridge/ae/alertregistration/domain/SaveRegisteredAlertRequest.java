package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SaveRegisteredAlertRequest {

  String alertName;

  String fkcoSystemId;

  List<SaveRegisteredMatchRequest> matches;
}
