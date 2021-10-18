package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;
import com.silenteight.payments.bridge.ae.alertregistration.domain.MatchQuantity;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.common.model.AlertRegistration;

import com.google.protobuf.Timestamp;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Value
public class LearningAlert {

  String alertId;

  String systemId;

  String messageId;

  @Setter(AccessLevel.PRIVATE)
  @NonFinal
  String alertName;

  Timestamp alertTime;

  List<LearningMatch> matches;

  String batchStamp;

  String fileName;

  int hitCount;

  String fircoAnalystDecision;  // Taken from FKCO_STATUS
  String fircoAnalystDecisionTime;  // Taken from FKCO_D_ACTION_DATETIME
  String fircoAnalystComment;  // FKCO_V_ACTION_COMMENT

  public AlertRegistration getAlertRegistration() {
    return new AlertRegistration(systemId, messageId);
  }

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }

  public RegisterAlertRequest toRegisterAlertRequest() {
    return RegisterAlertRequest
        .builder()
        .alertId(alertId)
        .alertTime(alertTime)
        .matchQuantity(matches.size() > 1 ? MatchQuantity.MANY : MatchQuantity.SINGLE)
        .matchIds(matches.stream().map(
            LearningMatch::getMatchId).collect(toList()))
        .labels(List.of(
            Label.builder().name("learningBatch").value(batchStamp).build(),
            Label.builder().name("fileName").value(fileName).build()))
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
