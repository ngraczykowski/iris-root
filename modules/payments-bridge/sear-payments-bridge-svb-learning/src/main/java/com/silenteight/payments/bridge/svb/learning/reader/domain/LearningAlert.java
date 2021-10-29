package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.common.model.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.common.model.RegisteredAlert;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromOffsetDateTime;
import static java.util.stream.Collectors.toList;

@Builder
@Value
public class LearningAlert {

  private static final int LEARNING_PRIORITY = 3;

  String alertId;

  String systemId;

  String messageId;

  @Setter(AccessLevel.PUBLIC)
  @NonFinal
  String alertName;

  OffsetDateTime alertTime;

  List<LearningMatch> matches;

  String batchStamp;

  String fileName;

  AnalystDecision analystDecision;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return new FindRegisteredAlertRequest(systemId, messageId);
  }

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }

  public RegisterAlertRequest toRegisterAlertRequest() {
    return RegisterAlertRequest
        .builder()
        .alertId(alertId)
        .alertTime(fromOffsetDateTime(alertTime))
        .priority(LEARNING_PRIORITY)
        .matchIds(matches.stream().map(LearningMatch::getMatchId).collect(toList()))
        .label(Label.of("learningBatch", batchStamp))
        .label(Label.of("fileName", fileName))
        .label(Label.of("source", "CSV"))
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

  public void setAlertMatchNames(RegisteredAlert response) {
    setAlertName(response.getAlertName());

  }
}
