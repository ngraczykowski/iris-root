package com.silenteight.bridge.core.reports.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.reports.domain.commands.SendReportsCommand;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportsFacade {

  private final ReportsService reportsService;

  public void sendReports(SendReportsCommand command) {
    reportsService.sendReports(command.batchId(), command.analysisName());
  }
}
