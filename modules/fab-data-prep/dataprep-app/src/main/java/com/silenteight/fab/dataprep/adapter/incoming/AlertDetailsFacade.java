package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.infrastructure.grpc.AlertDetailsServiceClient;
import com.silenteight.proto.fab.api.v1.*;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertDetailsFacade {

  private final AlertDetailsServiceClient alertDetailsServiceClient;

  public AlertMessagesDetailsResponse getAlertDetails(AlertMessageStored message) {
    AlertMessagesDetailsRequest detailsRequest = AlertMessagesDetailsRequest.newBuilder()
        .addAlerts(convert(message)).build();

    return alertDetailsServiceClient.get(detailsRequest);
  }

  public static AlertMessageHeader convert(AlertMessageStored source) {
    return AlertMessageHeader
        .newBuilder()
        .setMessageName(source.getMessageName())
        .setBatchName(source.getBatchName())
        .build();
  }


}
