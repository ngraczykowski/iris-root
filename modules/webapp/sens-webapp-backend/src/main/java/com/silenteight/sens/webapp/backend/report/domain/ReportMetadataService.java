package com.silenteight.sens.webapp.backend.report.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

import static com.silenteight.sep.base.common.logging.LogMarkers.INTERNAL;

@Slf4j
@RequiredArgsConstructor
public class ReportMetadataService {

  @NonNull
  private final ReportMetadataRepository repository;

  public OffsetDateTime getStartTime(String reportName) {
    log.debug(INTERNAL, "Get start time for reportName={}", reportName);

    return repository
        .findByReportName(reportName)
        .map(ReportMetadata::getStartTime)
        .orElse(null);
  }

  public void saveStartTime(String reportName, OffsetDateTime startTime) {
    log.debug(INTERNAL, "Save startTime={} for reportName={}", startTime, reportName);

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
