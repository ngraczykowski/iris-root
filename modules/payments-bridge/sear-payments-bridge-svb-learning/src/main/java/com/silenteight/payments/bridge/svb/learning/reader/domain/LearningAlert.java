package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Value
public class LearningAlert {

  long alertId;

  @Setter(AccessLevel.PRIVATE)
  @NonFinal
  String alertName;

  List<LearningMatch> matches;

  public RegisterAlertRequest toRegisterAlertRequest() {
    return RegisterAlertRequest.builder().alertId(alertId).matchIds(matches.stream().map(
        LearningMatch::getMatchId).collect(toList())).build();
  }

  public void setAlertMatchNames(RegisterAlertResponse response) {
    setAlertName(response.getAlertName());

    response.getMatchReponses().forEach(m -> {
      var match = matches.stream().filter(ma -> ma.getMatchId().equals(m.getMatchId())).findFirst();

      if (match.isEmpty())
        return;

      match.get().setMatchName(m.getMatchName());
    });
  }
}
