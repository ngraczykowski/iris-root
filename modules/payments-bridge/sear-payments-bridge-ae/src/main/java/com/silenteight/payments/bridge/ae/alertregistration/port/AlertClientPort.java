package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.adjudication.api.v1.BatchCreateAlertsRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertsResponse;
import com.silenteight.adjudication.api.v1.BatchCreateMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateMatchesResponse;

public interface AlertClientPort {

  BatchCreateAlertsResponse createAlert(BatchCreateAlertsRequest request);

  BatchCreateMatchesResponse createMatches(BatchCreateMatchesRequest request);
}
