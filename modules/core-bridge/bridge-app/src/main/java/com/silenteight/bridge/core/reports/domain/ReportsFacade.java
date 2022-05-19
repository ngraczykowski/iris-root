package com.silenteight.bridge.core.reports.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.reports.domain.commands.SendReportsCommand;
import com.silenteight.bridge.core.reports.infrastructure.ReportsProperties;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportsFacade {

  private final ReportsService reportsService;
  private final ReportsProperties properties;

  public void sendReports(SendReportsCommand command) {
    if (properties.streamingEnabled()) {
      reportsService.streamReports(command.batchId(), command.analysisName());
    } else {
      reportsService.sendReports(command.batchId(), command.analysisName());
    }
  }
}
