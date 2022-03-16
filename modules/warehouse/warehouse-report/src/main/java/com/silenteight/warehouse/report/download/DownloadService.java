package com.silenteight.warehouse.report.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.persistence.ReportDto;
import com.silenteight.warehouse.report.persistence.ReportPersistenceService;
import com.silenteight.warehouse.report.persistence.ReportRange;
import com.silenteight.warehouse.report.storage.FileDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.time.OffsetDateTime;
import java.util.Map;

import static com.silenteight.warehouse.common.domain.ReportConstants.IS_PRODUCTION;
import static com.silenteight.warehouse.common.domain.ReportConstants.PRODUCTION;
import static com.silenteight.warehouse.common.domain.ReportConstants.SIMULATION;
import static java.lang.String.valueOf;
import static java.time.ZoneOffset.UTC;

@RequiredArgsConstructor
class DownloadService {

  @NonNull
  private final ReportPersistenceService reportPersistenceService;
  @NonNull
  private final ReportStorage reportStorage;
  @NonNull
  private final Map<String, ReportFileName> reportNameResolvers;
  @NonNull
  private final DateFormatter dateFormatter;

  public DownloadReportDto getFor(String type, long id) {
    ReportDto dto = reportPersistenceService.getReport(id);
    FileDto report = reportStorage.getReport(dto.getFileStorageName());

    return DownloadReportDto.builder()
        .name(getFileName(dto, type))
        .content(report.getContent())
        .build();
  }

  private String getFileName(ReportDto dto, String type) {
    if (IS_PRODUCTION.test(type)) {
      ReportRange range = dto.getRange();
      return reportNameResolvers.get(PRODUCTION).getReportName(
          ReportFileNameDto.builder()
              .reportType(dto.getName())
              .from(dateFormatter.format(range.getFrom()))
              .to(dateFormatter.format(range.getTo()))
              .timestamp(toTimestamp(dto.getCreatedAt()))
              .extension(dto.getExtension().getFileExtension())
              .build()
      );
    } else {
      return reportNameResolvers.get(SIMULATION).getReportName(
          ReportFileNameDto.builder()
              .reportType(dto.getName())
              .analysisId(type)
              .timestamp(toTimestamp(dto.getCreatedAt()))
              .extension(dto.getExtension().getFileExtension())
              .build()
      );
    }
  }

  private static String toTimestamp(OffsetDateTime createdAt) {
    return valueOf(createdAt.atZoneSameInstant(UTC).toEpochSecond());
  }

}
