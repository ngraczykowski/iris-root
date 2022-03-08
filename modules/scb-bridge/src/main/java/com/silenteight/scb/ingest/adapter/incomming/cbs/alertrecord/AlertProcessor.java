package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class AlertProcessor {

  private final AlertInFlightService alertInFlightService;
  private final AlertCompositeCollectionReader alertCompositeCollectionReader;
  private final AlertHandler alertHandler;

  @Scheduled(fixedDelay = 1000, initialDelay = 2000)
  void process() {
    var alertIds = alertInFlightService.readChunk();

    if (alertIds.isEmpty()) {
      return;
    }

    log.info("Fetched alert ids: {}", alertIds.size());

    groupAlertIdsByContext(alertIds).entrySet()
        .parallelStream()
        .forEach((e) -> processAlertsWithTheSameContext(e.getKey(), e.getValue()));
  }

  private Map<ScbAlertIdContext, List<AlertId>> groupAlertIdsByContext(
      List<AlertIdWithDetails> alertIds) {
    return alertIds.stream()
        .collect(groupingBy(AlertIdWithDetails::getContext,
            mapping(AlertIdWithDetails::toAlertId, toList())));
  }

  private void processAlertsWithTheSameContext(ScbAlertIdContext context, List<AlertId> alertIds) {
    var alertCompositeCollection = readAlertComposites(alertIds, context);

    alertHandler.handleAlerts(context, alertCompositeCollection);
  }

  private AlertCompositeCollection readAlertComposites(
      List<AlertId> alertIds, ScbAlertIdContext context) {
    return alertCompositeCollectionReader.read(alertIds, context);
  }
}
