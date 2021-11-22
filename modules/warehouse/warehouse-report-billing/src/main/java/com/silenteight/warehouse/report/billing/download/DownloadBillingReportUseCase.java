package com.silenteight.warehouse.report.billing.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.billing.domain.dto.BillingReportDto;
import com.silenteight.warehouse.report.billing.download.dto.DownloadBillingReportDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

@Slf4j
@RequiredArgsConstructor
class DownloadBillingReportUseCase {

  private static final String REPORT_TYPE = "Billing";

  @NonNull
  private final ReportDataQuery reportDataQuery;
  @NonNull
  private final BillingReportService reportService;
  @NonNull
  private final ReportFileName reportFileName;

  DownloadBillingReportDto activate(long id) {
    log.debug("Getting Billing report, reportId={}", id);
    BillingReportDto dto = reportDataQuery.getReport(id);
    reportService.removeReport(id);
    log.debug("Report removed, reportId={}", id);

    return DownloadBillingReportDto.builder()
        .name(getFileName(dto))
        .content(dto.getContent())
        .build();
  }

  private String getFileName(BillingReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private static ReportFileNameDto toReportFileNameDto(BillingReportDto dto) {
    ReportRange range = dto.getRange();
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .from(range.getFromAsLocalDate().toString())
        .to(range.getToAsLocalDate().toString())
        .timestamp(dto.getTimestamp())
        .build();
  }
}
