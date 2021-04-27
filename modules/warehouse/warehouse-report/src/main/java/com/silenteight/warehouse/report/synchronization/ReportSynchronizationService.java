package com.silenteight.warehouse.report.synchronization;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
public class ReportSynchronizationService {

  private final ReportRepository reportRepository;

  public void markAsStored(String kibanaReportId, String tenant, String filename) {
    ReportEntity report = ReportEntity.builder()
        .kibanaReportInstanceId(kibanaReportId)
        .reportId(randomUUID())
        .filename(filename)
        .tenant(tenant)
        .build();

    reportRepository.save(report);
  }

  public Set<String> filterNew(Set<String> allKibanaReportInstanceIds) {
    Set<String> result = new HashSet<>(allKibanaReportInstanceIds);
    Set<String> synchronizedKibanaReportIds = reportRepository.getKibanaReportInstanceIds();
    result.removeAll(synchronizedKibanaReportIds);
    return result;
  }

  public Set<ReportDto> getAllReportsForTenant(String tenant) {
    return reportRepository.findByTenant(tenant).stream()
        .map(ReportEntity::toDto)
        .collect(toSet());
  }
}
