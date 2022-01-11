package com.silenteight.warehouse.report.billing.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
public class BillingReportService implements ReportsRemoval {

  @NonNull
  private final BillingReportRepository repository;
  @NonNull
  private final BillingReportAsyncGenerationService asyncReportGenerationService;
  @NonNull
  private final ReportStorage reportStorage;

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull List<String> indexes) {

    BillingReport report = BillingReport.of(range);
    BillingReport savedReport = repository.save(report);
    //FIXME(kdzieciol): Here we should send a request to the queue (internally) to generate this
    // report. Due to the lack of time, we will generate it in the thread (WEB-1358)
    asyncReportGenerationService.generateReport(savedReport.getId(), range, indexes);
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    var outdatedReports = repository.getAllByCreatedAtBefore(dayToRemoveReports);
    List<String> outdatedReportsFileNames = getOutdatedReportsFileNames(outdatedReports);
    reportStorage.removeReports(outdatedReportsFileNames);
    repository.deleteAll(outdatedReports);
    int numberOfRemovedReports = outdatedReports.size();
    log.info("Number of removed Billing reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }

  private static List<String> getOutdatedReportsFileNames(List<BillingReport> outdatedReports) {
    return outdatedReports.stream()
        .map(BillingReport::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }
}
