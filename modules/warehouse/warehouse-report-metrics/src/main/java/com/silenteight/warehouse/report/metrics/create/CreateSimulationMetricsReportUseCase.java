package com.silenteight.warehouse.report.metrics.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.metrics.generation.PropertiesDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.Instant.EPOCH;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;

@RequiredArgsConstructor
class CreateSimulationMetricsReportUseCase {

  private static final String FILE_NAME = "simulation-metrics.csv";

  @NonNull
  private final MetricsReportService reportService;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;
  @Valid
  @NonNull
  private final PropertiesDefinition simulationProperties;
  @NonNull
  private final TimeSource timeSource;

  ReportInstanceReferenceDto createReport(String analysisId) {
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    OffsetDateTime offsetDateNow = timeSource.offsetDateTime().withOffsetSameInstant(UTC);
    ReportRange range = of(ofInstant(EPOCH, UTC), offsetDateNow);
    return reportService.createReportInstance(range, FILE_NAME, indexes, simulationProperties);
  }
}
