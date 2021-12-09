package com.silenteight.warehouse.report.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.generation.dto.GenerateReportDto;
import com.silenteight.warehouse.report.reporting.Report;
import com.silenteight.warehouse.report.sql.SqlExecutor;
import com.silenteight.warehouse.report.storage.ReportStorage;

import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class GenerateReportService {

  @NonNull
  private final SqlExecutor sqlExecutor;
  @NonNull
  private final ReportStorage reportStorage;

  public void generate(GenerateReportDto dto) {
    sqlExecutor.execute(dto.getSqlExecutorDto(), is -> saveReportToMinio(is, dto.getReportName()));
  }

  private void saveReportToMinio(InputStream inputStream, String storageFileName) {
    Report report = toReport(inputStream, storageFileName);
    reportStorage.saveReport(report);
  }

  private static Report toReport(InputStream inputStream, String storageFileName) {
    return InMemoryReportDto.builder()
        .reportName(storageFileName)
        .inputStream(inputStream)
        .build();
  }
}
