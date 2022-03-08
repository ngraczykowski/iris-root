package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.proto.serp.v1.alert.Alert;

import java.util.stream.Stream;

public interface BatchAlertIngestService {

  void ingestAlertsForLearn(Stream<Alert> alerts);

  void ingestAlertsForRecommendation(Stream<Alert> alerts);
}
