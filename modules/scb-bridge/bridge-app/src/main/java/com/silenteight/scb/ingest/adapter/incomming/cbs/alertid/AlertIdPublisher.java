package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.BatchReadEvent;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.scb.ingest.domain.model.Batch.Priority;
import com.silenteight.scb.ingest.domain.model.BatchSource;
import com.silenteight.scb.ingest.domain.model.IngestBatchMessage;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestBatchEventPublisher;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

import static com.google.common.base.Strings.nullToEmpty;

@Slf4j
@Builder
class AlertIdPublisher implements Consumer<AlertIdCollection> {

  private final AlertInFlightService alertInFlightService;
  private final IngestBatchEventPublisher ingestBatchEventPublisher;
  private final BatchInfoService batchInfoService;

  @Override
  public void accept(@Nonnull AlertIdCollection collection) {
    if (log.isDebugEnabled())
      log.debug("Received alert IDs: {}", collection);

    var internalBatchId = InternalBatchIdGenerator.generate();
    var alertIdContext = toScbAlertIdContext(collection.getContext());

    batchInfoService.store(internalBatchId, BatchSource.CBS, collection.getSize());
    alertInFlightService.saveUniqueAlerts(
        collection.getAlertIds(), internalBatchId, alertIdContext);
    log.info(
        "Collection of {} alerts has been saved with internalBatchId: {}", collection.getSize(),
        internalBatchId);
    ingestBatchEventPublisher.publish(createIngestBatchMessage(internalBatchId));
  }

  private IngestBatchMessage createIngestBatchMessage(String internalBatchId) {
    return new IngestBatchMessage(new BatchReadEvent(internalBatchId), Priority.MEDIUM);
  }

  private static ScbAlertIdContext toScbAlertIdContext(AlertIdContext context) {
    return ScbAlertIdContext.newBuilder()
        .setAckRecords(context.isAckRecords())
        .setHitDetailsView(nullToEmpty(context.getHitDetailsView()))
        .setPriority(context.getPriority())
        .setSourceView(context.getRecordsView())
        .setWatchlistLevel(context.isWatchlistLevel())
        .build();
  }
}
