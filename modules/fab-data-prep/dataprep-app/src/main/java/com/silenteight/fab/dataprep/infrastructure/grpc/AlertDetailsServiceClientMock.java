package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.fab.api.v1.AlertDetails;
import com.silenteight.proto.fab.api.v1.AlertHeader;
import com.silenteight.proto.fab.api.v1.AlertsDetailsRequest;
import com.silenteight.proto.fab.api.v1.AlertsDetailsResponse;

import java.util.stream.Collectors;

@Slf4j
public class AlertDetailsServiceClientMock extends AlertDetailsServiceClient {

  AlertDetailsServiceClientMock() {
    super(null);
  }

  @Override
  public AlertsDetailsResponse get(AlertsDetailsRequest request) {
    return AlertsDetailsResponse
        .newBuilder()
        .addAllAlerts(
            request.getAlertsList().stream().map(this::convert).collect(Collectors.toList()))
        .build();
  }

  AlertDetails convert(AlertHeader request) {
    return AlertDetails
        .newBuilder()
        .setAlertId(request.getAlertId())
        .setPayload("Empty payload")
        .build();
  }

}
