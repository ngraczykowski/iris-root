package com.silenteight.hsbc.bridge.alert;

import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertMatchesRequestDto;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertMatchesResponseDto;
import com.silenteight.hsbc.bridge.alert.dto.BatchCreateAlertsResponseDto;

import java.util.Collection;

public interface AlertServiceApi {

  BatchCreateAlertsResponseDto batchCreateAlerts(Collection<String> alerts);

  BatchCreateAlertMatchesResponseDto batchCreateAlertMatches(
      BatchCreateAlertMatchesRequestDto command);
}
