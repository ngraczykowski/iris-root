package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.silenteight.payments.bridge.ae.alertregistration.domain.MatchQuantity;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;

import com.google.protobuf.Timestamp;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Value
public class LearningAlert {

  String alertId;

  @Setter(AccessLevel.PRIVATE)
  @NonFinal
  String alertName;

  Timestamp alertTime;

  List<LearningMatch> matches;

  int hitCount;

  public RegisterAlertRequest toRegisterAlertRequest() {
    return RegisterAlertRequest
        .builder()
        .alertId(alertId)
        .alertTime(alertTime)
        .matchQuantity(hitCount > 0 ? MatchQuantity.MULTIPLE : MatchQuantity.SINGLE)
        .matchIds(matches.stream().map(
            LearningMatch::getMatchId).collect(toList()))
        .build();
  }

  public void setAlertMatchNames(RegisterAlertResponse response) {
    setAlertName(response.getAlertName());

    response.getMatchResponses().forEach(m -> {
      var match = matches.stream().filter(ma -> ma.getMatchId().equals(m.getMatchId())).findFirst();

      if (match.isEmpty())
        return;

      match.get().setMatchName(m.getMatchName());
    });
  }
}
