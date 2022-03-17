package com.silenteight.scb.ingest.domain;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;

import java.util.List;

public interface AlertRegistrationService {

  RegistrationResponse registerAlertsAndMatches(String batchId, List<Alert> alerts);
}
