package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.infrastructure.grpc.AlertDetailsServiceClient;
import com.silenteight.proto.fab.api.v1.*;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertDetailsFacade {

  private final AlertDetailsServiceClient alertDetailsServiceClient;

  public AlertsDetailsResponse getAlertDetails(MessageAlertStored message) {
    AlertsDetailsRequest alertsDetailsRequest = AlertsDetailsRequest.newBuilder()
        .addAlerts(convert(message)).build();

    return alertDetailsServiceClient.get(alertsDetailsRequest);
  }

  public static AlertHeader convert(MessageAlertStored source) {
    return AlertHeader
        .newBuilder()
        .setAlertId(source.getAlertId())
        .setBatchId(source.getBatchId())
        .build();
  }


}
