package com.silenteight.serp.governance.qa.send;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.serp.governance.qa.manage.domain.DecisionLevel;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import com.google.protobuf.Struct;

import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Value(staticConstructor = "of")
public class SendAlertMessageCommand {

  private static final String STATE = "state";
  private static final String COMMENT = "comment";

  @NonNull
  List<AlertDto> alertDtos;

  ProductionDataIndexRequest toMessage() {
    return ProductionDataIndexRequest
        .newBuilder()
        .addAllAlerts(getAlerts())
        .build();
  }

  List<Alert> getAlerts() {
    return alertDtos.stream()
        .map(this::toAlert)
        .collect(toList());
  }

  Alert toAlert(AlertDto alertDto) {
    return Alert.newBuilder()
        .setName(alertDto.getDiscriminator())
        .setPayload(getPayload(alertDto))
        .build();
  }

  private static Struct getPayload(AlertDto alertDto) {
    return Struct.newBuilder()
        .putFields(getFieldName(alertDto.getLevel(), STATE),
            asStringValue(alertDto.getState().toString()))
        .putFields(getFieldName(alertDto.getLevel(), COMMENT),
            asStringValue(alertDto.getComment()))
        .build();
  }

  private static String getFieldName(DecisionLevel level, String fieldName) {
    return format("qa.level-%d.%s", level.getValue(), fieldName);
  }

  private static com.google.protobuf.Value asStringValue(String value) {
    if (value == null)
      return com.google.protobuf.Value.getDefaultInstance();

    return com.google.protobuf.Value.newBuilder()
        .setStringValue(value)
        .build();
  }
}
