package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand;
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand.DataRetentionAlert;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
class DataRetentionService {

  private final RawAlertService rawAlertService;

  void performDataRetention(DataRetentionCommand command) {
    log.info("Performing data retention on {} alerts", command.alerts().size());
    groupByInternalBatchId(command.alerts())
        .forEach(this::removeExpiredRawAlerts);
  }

  private void removeExpiredRawAlerts(String internalBatchId, List<DataRetentionAlert> alerts) {
    var systemIds = extractSystemIds(alerts);
    rawAlertService.removeExpiredAlerts(internalBatchId, systemIds);
  }

  private Set<String> extractSystemIds(List<DataRetentionAlert> alerts) {
    return alerts.stream()
        .map(DataRetentionAlert::systemId)
        .collect(Collectors.toUnmodifiableSet());
  }

  private Map<String, List<DataRetentionAlert>> groupByInternalBatchId(
      List<DataRetentionAlert> alerts) {
    return alerts.stream()
        .collect(Collectors.groupingBy(DataRetentionAlert::internalBatchId));
  }
}
