package com.silenteight.serp.governance.qa.send;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.data.api.v2.QaAlert.State;
import com.silenteight.data.api.v2.QaDataIndexRequest;
import com.silenteight.serp.governance.qa.manage.domain.DecisionState;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Value(staticConstructor = "of")
public class SendAlertMessageCommand {

  private static final String EMPTY_COMMENT = "";

  @NonNull
  List<AlertDto> alertDtos;

  QaDataIndexRequest toMessage() {
    return QaDataIndexRequest
        .newBuilder()
        .addAllAlerts(getAlerts())
        .build();
  }

  List<QaAlert> getAlerts() {
    return alertDtos.stream()
        .map(this::toAlert)
        .collect(toList());
  }

  QaAlert toAlert(AlertDto alertDto) {
    return QaAlert.newBuilder()
        .setName(alertDto.getAlertName())
        .setLevel(alertDto.getLevel().getValue())
        .setState(parseState(alertDto.getState()))
        .setComment(parseComment(alertDto.getComment()))
        .build();
  }

  private static State parseState(DecisionState decisionState) {
    return State.valueOf(decisionState.name());
  }

  private static String parseComment(String comment) {
    return Objects.toString(comment, EMPTY_COMMENT);
  }
}
