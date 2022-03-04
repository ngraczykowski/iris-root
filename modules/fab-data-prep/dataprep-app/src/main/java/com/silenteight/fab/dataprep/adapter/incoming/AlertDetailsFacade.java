package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.MessageAlertAndMatchesStoredToAlertHeaderConverter;
import com.silenteight.fab.dataprep.infrastructure.grpc.AlertDetailsServiceClient;
import com.silenteight.proto.fab.api.v1.AlertsDetailsRequest;
import com.silenteight.proto.fab.api.v1.AlertsDetailsResponse;
import com.silenteight.proto.fab.api.v1.MessageAlertAndMatchesStored;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertDetailsFacade {

  private final AlertDetailsServiceClient alertDetailsServiceClient;

  private final MessageAlertAndMatchesStoredToAlertHeaderConverter
      messageAlertAndMatchesStoredToAlertHeaderConverter =
      new MessageAlertAndMatchesStoredToAlertHeaderConverter();

  public AlertsDetailsResponse getAlertDetails(MessageAlertAndMatchesStored message) {
    AlertsDetailsRequest alertsDetailsRequest = AlertsDetailsRequest.newBuilder()
        .addAlerts(messageAlertAndMatchesStoredToAlertHeaderConverter.convert(message)).build();

    return alertDetailsServiceClient.get(alertsDetailsRequest);
  }
}
