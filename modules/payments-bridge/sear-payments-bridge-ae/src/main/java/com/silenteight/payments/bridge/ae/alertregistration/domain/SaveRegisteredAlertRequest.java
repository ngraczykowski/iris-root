package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class SaveRegisteredAlertRequest {

  UUID alertId;

  String alertName;

  List<SaveRegisteredMatchRequest> matches;
}
