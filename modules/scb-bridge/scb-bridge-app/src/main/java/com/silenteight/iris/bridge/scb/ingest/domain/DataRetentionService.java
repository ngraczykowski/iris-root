/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertService;
import com.silenteight.iris.bridge.scb.ingest.domain.model.DataRetentionCommand;
import com.silenteight.iris.bridge.scb.ingest.domain.model.DataRetentionCommand.DataRetentionAlert;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataRetentionService {

  private final RawAlertService rawAlertService;

  public void performDataRetention(DataRetentionCommand command) {
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
