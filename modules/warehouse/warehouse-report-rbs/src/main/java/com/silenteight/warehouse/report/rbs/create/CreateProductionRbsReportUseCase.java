package com.silenteight.warehouse.report.rbs.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.format;

@RequiredArgsConstructor
class CreateProductionRbsReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String FILE_NAME = "RB_Scorer_%s_To_%s.csv";

  @NonNull
  private final RbsReportService reportService;
  @NonNull
  private final IndexesQuery productionIndexerQuery;
  @Valid
  @NonNull
  private final RbsReportDefinition productionProperties;

  ReportInstanceReferenceDto createReport(LocalDate from, LocalDate to) {
    ReportRange range = of(from, to);
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    String fileName = getFileName(from, to);
    return reportService.createReportInstance(range, fileName, indexes, productionProperties);
  }

  private static String getFileName(LocalDate from, LocalDate to) {
    return format(FILE_NAME, from, to);
  }
}
