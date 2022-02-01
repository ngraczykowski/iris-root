package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.silenteight.payments.bridge.ae.alertregistration.domain.FindRegisteredAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.silenteight.payments.bridge.common.app.AlertLabelUtils.ALERT_LABEL_LEARNING;
import static com.silenteight.payments.bridge.common.app.AlertLabelUtils.ALERT_LABEL_LEARNING_CSV;
import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromOffsetDateTime;
import static java.util.stream.Collectors.toList;

@Builder
@Value
public class LearningAlert {

  private static final int LEARNING_PRIORITY = 3;

  UUID alertMessageId;

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

  @Setter(AccessLevel.PUBLIC)
  @NonFinal
  String decision;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return FindRegisteredAlertRequest.builder().systemId(systemId).messageId(messageId).build();
  }

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }

  public RegisterAlertRequest toRegisterAlertRequest() {
    return RegisterAlertRequest
        .builder()
        .alertMessageId(alertMessageId)
        .fkcoSystemId(systemId)
        .alertTime(fromOffsetDateTime(alertTime))
        .priority(LEARNING_PRIORITY)
        .matchIds(matches.stream().map(LearningMatch::getMatchId).collect(toList()))
        .label(Label.of("learningBatch", batchStamp))
        .label(Label.of("fileName", fileName))
        .label(getAlertLabelLearningCsv())
        .build();
  }

  public void setAlertMatchNames(RegisterAlertResponse response) {
    setAlertName(response.getAlertName());

    var matchResponses = response.getMatchResponses();

    for (var matchResponse : matchResponses) {

      matches.stream()
          .filter(match -> match.getMatchId().equals(matchResponse.getMatchId()))
          .findFirst()
          .ifPresent(match -> match.setMatchName(matchResponse.getMatchName()));
    }
  }

  public static Label getAlertLabelLearningCsv() {
    return Label.of(ALERT_LABEL_LEARNING, ALERT_LABEL_LEARNING_CSV);
  }

}
