package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.stream.Stream;

public interface BatchAlertIngestService {

  void ingestAlertsForLearn(Stream<Alert> alerts);

  void ingestAlertsForRecommendation(Stream<Alert> alerts);
}
