package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.BatchReadEvent;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.domain.model.BatchStatus;

import io.vavr.control.Try;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Builder
@Slf4j
class BatchProcessingEventListener {

  private final AlertInFlightService alertInFlightService;
  private final AlertCompositeCollectionReader alertCompositeCollectionReader;
  private final AlertHandler alertHandler;
  private final BatchInfoService batchInfoService;

  @RabbitListener(queues = "${amqp.ingest.incoming.batch-processing.queue-name}", concurrency = "1")
  void subscribe(BatchReadEvent batchReadEvent) {
    log.info("Received: {}", batchReadEvent);

    var internalBatchId = batchReadEvent.internalBatchId();
    var stopWatch = StopWatch.createStarted();

    Try.run(() -> processInternalBatch(internalBatchId))
        .onSuccess(__ -> batchInfoService.changeStatus(internalBatchId, BatchStatus.PROCESSING))
        .onFailure(e -> {
          batchInfoService.changeStatus(internalBatchId, BatchStatus.ERROR);
          log.error(
              "Error occurred while handling batch with internalBatchId: {}", internalBatchId, e);
        })
        .andFinally(() -> log.info("Processing: {}, executed in: {}", batchReadEvent, stopWatch));
  }

  private void processInternalBatch(String internalBatchId) {
    var alertIds = alertInFlightService.getAlertsFromBatch(internalBatchId);

    if (alertIds.isEmpty()) {
      throw new IllegalStateException(
          "No alerts were found for batch with internalBatchId: " + internalBatchId);
    }

    log.info("Fetched alert ids: {} for internalBatchId: {}", alertIds.size(), internalBatchId);

    var alertCompositeCollections =
        groupAlertIdsByContext(alertIds).entrySet()
            .parallelStream()
            .map(entry -> readAlertComposites(entry.getValue(), internalBatchId, entry.getKey()))
            .toList();
    alertHandler.handleAlerts(internalBatchId, alertCompositeCollections);
  }

  private Map<ScbAlertIdContext, List<AlertId>> groupAlertIdsByContext(
      List<AlertIdWithDetails> alertIds) {
    return alertIds.stream()
        .collect(groupingBy(
            AlertIdWithDetails::getContext,
            mapping(AlertIdWithDetails::toAlertId, toList())));
  }

  private AlertCompositeCollection readAlertComposites(
      List<AlertId> alertIds, String internalBatchId, ScbAlertIdContext context) {
    return alertCompositeCollectionReader.read(alertIds, internalBatchId, context);
  }
}
