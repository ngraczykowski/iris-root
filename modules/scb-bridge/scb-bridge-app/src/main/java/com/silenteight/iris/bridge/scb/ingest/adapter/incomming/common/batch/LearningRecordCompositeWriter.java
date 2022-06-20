/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.experimental.SuperBuilder;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest.BatchAlertIngestService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;

import java.util.List;

@SuperBuilder
class LearningRecordCompositeWriter extends RecordCompositeWriter {

  private final BatchAlertIngestService ingestService;

  protected void writeAlerts(List<? extends AlertComposite> items) {
    var internalBatchId = InternalBatchIdGenerator.generate();
    var alerts = items.stream()
        .map(AlertComposite::getAlert)
        .toList();
    ingestService.ingestAlertsForLearn(internalBatchId, alerts);
  }
}
