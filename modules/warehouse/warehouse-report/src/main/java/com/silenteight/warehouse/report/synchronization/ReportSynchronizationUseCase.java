package com.silenteight.warehouse.report.synchronization;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportingService;
import com.silenteight.warehouse.report.storage.InMemoryReport;
import com.silenteight.warehouse.report.storage.Report;
import com.silenteight.warehouse.report.storage.ReportStorageService;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class ReportSynchronizationUseCase {

  @NonNull
  private final ReportingService reportingService;

  @NonNull
  private final ReportSynchronizationService reportSynchronizationService;

  @NonNull
  private final ReportStorageService reportStorageService;

  @NonNull
  private final String productionTenant;

  @Scheduled(cron = "${warehouse.report.synchronization.cron}")
  public void activate() {
    log.info("Report synchronization started");
    getNewReportIds().forEach(this::synchronizeSingleReport);
  }

  private Set<String> getNewReportIds() {
    Set<String> allKibanaReportInstanceIds = reportingService.getReportList(productionTenant);
    return reportSynchronizationService.filterNew(allKibanaReportInstanceIds);
  }

  private void synchronizeSingleReport(String kibanaReportInstanceId) {
    try {
      Report report = downloadReportFromKibana(kibanaReportInstanceId);
      ReportDto storedReportDto = uploadReportToMinio(report, kibanaReportInstanceId);
      log.debug("Report synchronized, {}", storedReportDto);
    } catch (RuntimeException e) {
      String msg = format("Failed to synchronize report: tenant=%s, kibanaReportInstanceId=%s",
          productionTenant, kibanaReportInstanceId);
      log.error(msg, e);
    }
  }

  private Report downloadReportFromKibana(String kibanaReportInstanceId) {
    KibanaReportDto kibanaReportDto = reportingService.getReport(
        productionTenant, kibanaReportInstanceId);
    return new InMemoryReport(
        kibanaReportDto.getFilename(),
        kibanaReportDto.getContent().getBytes());
  }

  private ReportDto uploadReportToMinio(Report report, String kibanaReportInstanceId) {
    reportStorageService.saveReport(report);
    return reportSynchronizationService.markAsStored(
        kibanaReportInstanceId, productionTenant, report.getReportName());
  }
}
