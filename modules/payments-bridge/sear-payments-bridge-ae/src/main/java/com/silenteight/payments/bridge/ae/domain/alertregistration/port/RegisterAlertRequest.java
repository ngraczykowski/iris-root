package com.silenteight.payments.bridge.ae.domain.alertregistration.port;

import lombok.Value;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.Match;

import java.util.List;

@Value
public class RegisterAlertRequest {

  Alert alert;

  List<Match> matches;
}
