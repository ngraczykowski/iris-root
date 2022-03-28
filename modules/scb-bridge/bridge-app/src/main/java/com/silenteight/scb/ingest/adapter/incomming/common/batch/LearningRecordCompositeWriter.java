package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.experimental.SuperBuilder;

import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;

import java.util.List;

@SuperBuilder
class LearningRecordCompositeWriter extends RecordCompositeWriter {

  private final BatchAlertIngestService ingestService;

  protected void writeAlerts(List<? extends AlertComposite> items) {
    var alerts = items.stream()
        .map(AlertComposite::getAlert)
        .toList();
    ingestService.ingestAlertsForLearn(InternalBatchIdGenerator.generate(), alerts);
  }
}
