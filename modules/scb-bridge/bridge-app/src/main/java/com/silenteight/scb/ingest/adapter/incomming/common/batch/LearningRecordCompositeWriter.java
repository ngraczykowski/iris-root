package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.experimental.SuperBuilder;

import com.silenteight.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.scb.ingest.domain.model.BatchSource;

import java.util.List;

@SuperBuilder
class LearningRecordCompositeWriter extends RecordCompositeWriter {

  private final BatchAlertIngestService ingestService;
  private final BatchInfoService batchInfoService;

  protected void writeAlerts(List<? extends AlertComposite> items) {
    var internalBatchId = InternalBatchIdGenerator.generate();
    batchInfoService.store(internalBatchId, BatchSource.LEARNING, items.size());
    var alerts = items.stream()
        .map(AlertComposite::getAlert)
        .toList();
    ingestService.ingestAlertsForLearn(internalBatchId, alerts);
  }
}
