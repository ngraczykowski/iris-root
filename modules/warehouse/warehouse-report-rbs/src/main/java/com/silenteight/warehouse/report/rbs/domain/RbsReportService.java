package com.silenteight.warehouse.report.rbs.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.rbs.domain.RbsReport.of;

@RequiredArgsConstructor
@Slf4j
public class RbsReportService implements ReportsRemoval {

  @NonNull
  private final RbsReportRepository repository;
  @NonNull
  private final AsyncRbsReportGenerationService asyncReportGenerationService;

  public ReportInstanceReferenceDto createReportInstance(
      @NonNull ReportRange range,
      @NonNull List<String> indexes,
      @NonNull @Valid RbsReportDefinition properties,
      String analysisId) {

    RbsReport report = of(range);
    RbsReport savedReport = repository.save(report);
    //FIXME(kdzieciol): Here we should send a request to the queue (internally) to generate this
    // report. Due to the lack of time, we will generate it in the thread (WEB-1358)
    asyncReportGenerationService.generateReport(savedReport.getId(), range, indexes, properties,
        savedReport.getFileName(), analysisId);
    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(long id) {
    repository.deleteById(id);
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    long numberOfRemovedReports = repository.deleteByCreatedAtBefore(dayToRemoveReports);
    log.info("Number of removed RB Scorer reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }
}
