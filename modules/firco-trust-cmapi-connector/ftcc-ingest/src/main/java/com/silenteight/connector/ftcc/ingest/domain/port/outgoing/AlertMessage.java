package com.silenteight.connector.ftcc.ingest.domain.port.outgoing;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.proto.fab.api.v1.AlertMessageStored;

import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

@Value
@Builder
public class AlertMessage {

  @NonNull
  String batchName;
  @NonNull
  String messageName;
  @NonNull
  State state;
  int priority;

  public AlertMessageStored toAlertMessageStored() {
    return AlertMessageStored.newBuilder()
        .setBatchName(getBatchName())
        .setMessageName(getMessageName())
        .setState(getState())
        .build();
  }
}
