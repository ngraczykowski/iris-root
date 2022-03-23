package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.experimental.SuperBuilder;

import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.List;
import java.util.stream.Stream;

@SuperBuilder
class LearningRecordCompositeWriter extends RecordCompositeWriter {

  private final BatchAlertIngestService ingestService;

  protected void writeAlerts(List<? extends AlertComposite> items) {
    Stream<Alert> alerts = items.stream().map(AlertComposite::getAlert);
    ingestService.ingestAlertsForLearn(alerts);
  }
}
