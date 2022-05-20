package com.silenteight.hsbc.bridge.alert;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertMatchesRequestDto;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertMatchesResponseDto;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertsResponseDto;

import java.util.List;

public interface AlertServiceClient {

  BatchCreateAlertsResponseDto batchCreateAlerts(List<Alert> alerts);

  BatchCreateAlertMatchesResponseDto batchCreateAlertMatches(
      BatchCreateAlertMatchesRequestDto command);
}
