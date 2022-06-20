/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.reports.domain.model;

import lombok.experimental.UtilityClass;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision;
import com.silenteight.iris.bridge.scb.reports.domain.model.Report.AlertData;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ReportMapper {

  public List<Report> toReports(List<Alert> alerts) {
    return alerts.stream()
        .map(ReportMapper::toReport)
        .collect(Collectors.toList());
  }

  public Report toReport(Alert alert) {
    if (alert.decisions().isEmpty()) {
      throw new IllegalStateException(
          "Learning " + alert.logInfo() + " does not have any Decisions");
    }
    // TODO: should we send just latest one (at index 0) or for all of them ?
    Decision decision = alert.decisions().get(0);
    return new Report(
        AlertData.builder()
            .alertName(alert.details().getAlertName())
            .id(alert.id().sourceId())
            .analystDecision(decision.solution().name())
            .analystDecisionModifiedDateTime(
                OffsetDateTime.ofInstant(decision.createdAt(), ZoneId.systemDefault()))
            .analystReason(decision.comment())
            .build());
  }
}
