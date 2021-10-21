package com.silenteight.warehouse.report.rbs.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.LocalDate.EPOCH;

@RequiredArgsConstructor
class CreateSimulationRbsReportUseCase {

  private static final String FILE_NAME = "simulation-rbscorer.csv";

  @NonNull
  private final RbsReportService reportService;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;
  @Valid
  @NonNull
  private final RbsReportDefinition simulationProperties;
  @NonNull
  private final TimeSource timeSource;

  ReportInstanceReferenceDto createReport(String analysisId) {
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    LocalDate localDateNow = timeSource.localDateTime().toLocalDate();
    ReportRange range = of(EPOCH, localDateNow);
    return reportService.createReportInstance(range, FILE_NAME, indexes, simulationProperties);
  }
}
