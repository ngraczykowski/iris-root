package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.RegistrationAlertContext;

import java.util.List;
import java.util.stream.Stream;

public interface BatchAlertIngestService {

  void ingestAlertsForLearn(Stream<Alert> alerts);

  void ingestAlertsForRecommendation(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationAlertContext registrationAlertContext);
}
