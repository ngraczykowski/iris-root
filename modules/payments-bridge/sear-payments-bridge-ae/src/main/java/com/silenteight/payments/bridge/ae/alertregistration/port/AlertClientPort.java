package com.silenteight.payments.bridge.ae.alertregistration.port;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesRequest;
import com.silenteight.adjudication.api.v1.BatchCreateAlertMatchesResponse;
import com.silenteight.adjudication.api.v1.CreateAlertRequest;

public interface AlertClientPort {

  Alert createAlert(CreateAlertRequest request);

  BatchCreateAlertMatchesResponse createMatches(BatchCreateAlertMatchesRequest request);
}
