package com.silenteight.warehouse.report.billing.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.report.billing.domain.dto.BillingReportDto;
import com.silenteight.warehouse.report.billing.download.dto.DownloadBillingReportDto;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.reporting.ReportRange;
import com.silenteight.warehouse.report.storage.ReportStorage;

@Slf4j
@RequiredArgsConstructor
class DownloadBillingReportUseCase {

  private static final String REPORT_TYPE = "Billing";

  @NonNull
  private final ReportDataQuery reportDataQuery;
  @NonNull
  private final ReportStorage reportStorageService;
  @NonNull
  private final ReportFileName reportFileName;
  @NonNull
  private final DateFormatter dateFormatter;

  DownloadBillingReportDto activate(long id) {
    log.debug("Getting Billing report, reportId={}", id);
    BillingReportDto dto = reportDataQuery.getReport(id);

    String fileStorageName = dto.getFileStorageName();
    log.debug("Getting Billing report from storage, fileStorageName={}", fileStorageName);
    FileDto report = reportStorageService.getReport(fileStorageName);

    return DownloadBillingReportDto.builder()
        .name(getFileName(dto))
        .content(report.getContent())
        .build();
  }

  private String getFileName(BillingReportDto dto) {
    ReportFileNameDto reportFileNameDto = toReportFileNameDto(dto);
    return reportFileName.getReportName(reportFileNameDto);
  }

  private ReportFileNameDto toReportFileNameDto(BillingReportDto dto) {
    ReportRange range = dto.getRange();
    return ReportFileNameDto.builder()
        .reportType(REPORT_TYPE)
        .from(dateFormatter.format(range.getFrom()))
        .to(dateFormatter.format(range.getTo()))
        .timestamp(dto.getTimestamp())
        .build();
  }
}
