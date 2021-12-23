package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class RegisteredAlertWithMatches {

  UUID alertId;
  String alertName;
  List<RegisteredMatch> matches;

}
