package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.BatchReadEvent;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class BatchProcessingEventListener {

  private final AlertInFlightService alertInFlightService;
  private final AlertCompositeCollectionReader alertCompositeCollectionReader;
  private final AlertHandler alertHandler;

  @RabbitListener(queues = "${amqp.ingest.incoming.batch-processing.queue-name}")
  void subscribe(BatchReadEvent batchReadEvent) {
    String internalBatchId = batchReadEvent.internalBatchId();
    log.info("Received a BatchReadEvent with internalBatchId: {}", internalBatchId);

    var alertIds = alertInFlightService.getAlertsFromBatch(internalBatchId);

    if (alertIds.isEmpty()) {
      log.trace("No alerts were found for batch with internalBatchId: {}", internalBatchId);
      return;
    }

    log.info("Fetched alert ids: {}", alertIds.size());

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
