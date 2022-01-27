package com.silenteight.warehouse.report.generation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.persistence.ReportDto;
import com.silenteight.warehouse.report.persistence.ReportPersistenceService;
import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.InMemoryReportDto;
import com.silenteight.warehouse.report.reporting.Report;
import com.silenteight.warehouse.report.sql.SqlExecutor;
import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;
import com.silenteight.warehouse.report.storage.ReportStorage;

import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class GenerationService implements ReportGenerationService, ReportsRemoval {

  @NonNull
  private final ReportPersistenceService reportPersistenceService;
  @NonNull
  private final ReportStorage reportStorage;
  @NonNull
  private final SqlExecutor sqlExecutor;

  @Async
  public void generate(ReportRequestData request) {
    try {
      reportPersistenceService.generationStarted(request.getDomainId());
      doGenerate(request);
      reportPersistenceService.generationSuccessful(request.getDomainId());

      log.debug(
          "Report generation done, id={}, fileStorageName={}",
          request.getDomainId(),
          request.getFileStorageName());

    } catch (RuntimeException e) {
      doFail(request.getDomainId());
      throw new IllegalStateException(e);
    }
  }

  public void doGenerate(ReportRequestData request) {
    SqlExecutorDto dto = QueryParamsSubstitutor.substitute(request);
    sqlExecutor.execute(
        dto,
        inputStream -> reportStorage.saveReport(
            toReport(request.getFileStorageName(), inputStream)));
  }

  private void doFail(Long id) {
    reportPersistenceService.generationFail(id);
    log.warn("Report generation failed, id={}", id);
  }

  private static Report toReport(String name, InputStream inputStream) {
    return InMemoryReportDto.builder()
        .reportName(name)
        .inputStream(inputStream)
        .build();
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    List<ReportDto> toRemove = reportPersistenceService.getAllByCreatedAtBefore(dayToRemoveReports);
    List<String> outdatedReportsFileNames = getOutdatedReportsFileNames(toRemove);
    reportStorage.removeReports(outdatedReportsFileNames);
    reportPersistenceService.deleteAll(toRemove);
    log.info("Number of removed reports reportsCount={}", toRemove.size());
    return toRemove.size();
  }

  private static List<String> getOutdatedReportsFileNames(List<ReportDto> outdatedReports) {
    return outdatedReports.stream()
        .map(ReportDto::getFileStorageName)
        .filter(Objects::nonNull)
        .collect(toList());
  }
}
