package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.fab.api.v1.*;

import java.util.stream.Collectors;

@Slf4j
public class AlertDetailsServiceClientMock extends AlertDetailsServiceClient {

  AlertDetailsServiceClientMock() {
    super(null, null);
  }

  @Override
  public AlertMessagesDetailsResponse get(AlertMessagesDetailsRequest request) {
    return AlertMessagesDetailsResponse
        .newBuilder()
        .addAllAlerts(
            request.getAlertsList().stream().map(this::convert).collect(Collectors.toList()))
        .build();
  }

  AlertMessageDetails convert(AlertMessageHeader request) {
    return AlertMessageDetails
        .newBuilder()
        .setMessageName(request.getMessageName())
        .setPayload("Empty payload")
        .build();
  }

}
