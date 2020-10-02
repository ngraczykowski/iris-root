package com.silenteight.sens.webapp.backend.report.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class ReportMetadataService {

  @NonNull
  private final ReportMetadataRepository repository;

  public OffsetDateTime getStartTime(String reportName) {
    return repository
        .findByReportName(reportName)
        .map(ReportMetadata::getStartTime)
        .orElse(null);
  }

  public void saveStartTime(String reportName, OffsetDateTime startTime) {
    repository
        .findByReportName(reportName)
        .ifPresentOrElse(
            reportMetadata -> this.updateReportMetadata(reportMetadata, startTime),
            () -> this.createReportMetadata(reportName, startTime));
  }

  private void updateReportMetadata(ReportMetadata reportMetadata, OffsetDateTime startTime) {
    repository.updateStartTime(reportMetadata.getReportName(), startTime);
  }

  private void createReportMetadata(String reportName, OffsetDateTime startTime) {
    ReportMetadata reportMetadata = new ReportMetadata(reportName, startTime);
    repository.save(reportMetadata);
  }
}
