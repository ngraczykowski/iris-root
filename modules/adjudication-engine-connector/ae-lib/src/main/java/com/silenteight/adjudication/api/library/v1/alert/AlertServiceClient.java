package com.silenteight.adjudication.api.library.v1.alert;

import java.util.List;

public interface AlertServiceClient {

  BatchCreateAlertsOut batchCreateAlerts(List<AlertIn> alerts);

  BatchCreateAlertMatchesOut batchCreateAlertMatches(BatchCreateAlertMatchesIn command);
}
