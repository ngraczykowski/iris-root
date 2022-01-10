package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.ae.alertregistration.domain.Label;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.FindRegisteredAlertRequest;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.payments.bridge.common.app.AlertLabelUtils.ALERT_LABEL_JOB_ID;
import static com.silenteight.payments.bridge.common.app.AlertLabelUtils.ALERT_LABEL_LEARNING;
import static com.silenteight.payments.bridge.common.app.AlertLabelUtils.ALERT_LABEL_LEARNING_CSV;
import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromOffsetDateTime;
import static java.util.stream.Collectors.toList;

@Value
@Builder
public class AlertDetails {

  private static final int LEARNING_PRIORITY = 3;

  long alertId;

  long fkcoId;

  String fkcoVFormat;

  String fkcoVApplication;

  OffsetDateTime fkcoDFilteredDateTime;

  String systemId;

  String messageId;

  String fkcoVContent;

  public FindRegisteredAlertRequest toFindRegisterAlertRequest() {
    return FindRegisteredAlertRequest.builder().messageId(messageId).systemId(systemId).build();
  }

  public RegisterAlertRequest toRegisterAlertRequest(Long jobId, List<HitComposite> hits) {
    return RegisterAlertRequest
        .builder()
        .alertId(systemId)
        .alertTime(fromOffsetDateTime(fkcoDFilteredDateTime))
        .priority(LEARNING_PRIORITY)
        .matchIds(hits.stream().map(HitComposite::getMatchId).collect(toList()))
        .label(Label.of(ALERT_LABEL_JOB_ID, jobId.toString()))
        .label(getAlertLabelLearningCsv())
        .build();
  }

  public static Label getAlertLabelLearningCsv() {
    return Label.of(ALERT_LABEL_LEARNING, ALERT_LABEL_LEARNING_CSV);
  }
}
