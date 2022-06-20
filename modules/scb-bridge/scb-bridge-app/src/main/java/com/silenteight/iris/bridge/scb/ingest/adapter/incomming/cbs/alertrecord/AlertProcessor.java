/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator;
import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails;
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchSource;
import com.silenteight.iris.bridge.scb.ingest.domain.model.BatchStatus;

import io.vavr.control.Try;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Slf4j
@Builder
@RequiredArgsConstructor
class AlertProcessor {

  private final AlertInFlightService alertInFlightService;
  private final AlertCompositeCollectionReader alertCompositeCollectionReader;
  private final AlertHandler alertHandler;
  private final BatchInfoService batchInfoService;
  private final TrafficManager trafficManager;

  @Scheduled(fixedDelayString = "${silenteight.scb-bridge.solving.alert-processor.fixed-delay}",
      initialDelayString = "${silenteight.scb-bridge.solving.alert-processor.initial-delay}")
  void process() {
    if (trafficManager.holdPeriodicAlertProcessing()) {
      log.trace("Alert processor on hold");
      return;
    }

    var alertIds = alertInFlightService.readChunk();
    if (alertIds.isEmpty()) {
      return;
    }

    groupAlertIdsByContext(alertIds).entrySet()
        .parallelStream()
        .forEach(entry -> processAlertsWithTheSameContext(entry.getKey(), entry.getValue()));
  }

  private Map<ScbAlertIdContext, List<AlertId>> groupAlertIdsByContext(
      List<AlertIdWithDetails> alertIds) {
    return alertIds.stream()
        .collect(groupingBy(
            AlertIdWithDetails::getContext,
            mapping(AlertIdWithDetails::toAlertId, toList())));
  }

  private void processAlertsWithTheSameContext(ScbAlertIdContext context, List<AlertId> alertIds) {
    var internalBatchId = InternalBatchIdGenerator.generate();
    batchInfoService.store(internalBatchId, BatchSource.CBS, alertIds.size());

    var stopWatch = StopWatch.createStarted();
    Try.run(() -> processInternalBatch(internalBatchId, context, alertIds))
        .onSuccess(__ -> batchInfoService.changeStatus(internalBatchId, BatchStatus.REGISTERED))
        .onFailure(e -> {
          batchInfoService.changeStatus(internalBatchId, BatchStatus.ERROR);
          log.error(
              "Error occurred while handling batch with internalBatchId: {}", internalBatchId, e);
        })
        .andFinally(
            () -> log.info("Processing of batch: {}, executed in: {}", internalBatchId, stopWatch));
  }

  private void processInternalBatch(
      String internalBatchId, ScbAlertIdContext context, List<AlertId> alertIds) {
    log.info("Processing internalBatchId: {} with {} alerts", internalBatchId, alertIds.size());
    var alertCompositeCollection = readAlertComposites(internalBatchId, context, alertIds);
    alertHandler.handleAlerts(internalBatchId, context, alertCompositeCollection);
  }

  private AlertCompositeCollection readAlertComposites(
      String internalBatchId, ScbAlertIdContext context, List<AlertId> alertIds) {
    return alertCompositeCollectionReader.read(internalBatchId, context, alertIds);
  }
}
