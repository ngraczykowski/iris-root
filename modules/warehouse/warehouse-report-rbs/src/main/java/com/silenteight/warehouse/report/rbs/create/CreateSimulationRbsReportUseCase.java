package com.silenteight.warehouse.report.rbs.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.reporting.RbsReportDefinition;
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
class CreateSimulationRbsReportUseCase {

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
    OffsetDateTime offsetDateNow = timeSource.offsetDateTime().withOffsetSameInstant(UTC);
    ReportRange range = of(ofInstant(EPOCH, UTC), offsetDateNow);
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    return reportService.createReportInstance(range, indexes, simulationProperties, analysisId);
  }
}
