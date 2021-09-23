package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RegisterAlertResponse {

  String alertName;

  List<RegisterMatchResponse> matchReponses;
}
