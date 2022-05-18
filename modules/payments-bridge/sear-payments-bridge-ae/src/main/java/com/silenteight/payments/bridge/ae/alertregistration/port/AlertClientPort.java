package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.adjudication.api.v1.*;

public interface AlertClientPort {

  Alert createAlert(CreateAlertRequest request);

  BatchCreateAlertsResponse batchCreateAlerts(BatchCreateAlertsRequest request);

  BatchCreateMatchesResponse batchCreateMatches(BatchCreateMatchesRequest request);

  BatchCreateAlertMatchesResponse createMatches(BatchCreateAlertMatchesRequest request);

  BatchAddLabelsResponse batchAddLabels(BatchAddLabelsRequest request);
}
