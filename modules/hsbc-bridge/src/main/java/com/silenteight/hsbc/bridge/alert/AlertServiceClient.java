package com.silenteight.hsbc.bridge.alert;

import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertMatchesRequestDto;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertMatchesResponseDto;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertsResponseDto;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

public interface AlertServiceClient {

  BatchCreateAlertsResponseDto batchCreateAlerts(Stream<AlertForCreation> alerts);

  BatchCreateAlertMatchesResponseDto batchCreateAlertMatches(
      BatchCreateAlertMatchesRequestDto command);

  interface AlertForCreation {

    String getId();
    OffsetDateTime getAlertTime();
  }
}
