package com.silenteight.warehouse.report.rbs.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.generation.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.format;

@RequiredArgsConstructor
class CreateProductionRbsReportUseCase {

  private static final String FILE_NAME = "RB_Scorer_%s_To_%s.csv";

  @NonNull
  private final RbsReportService reportService;
  @Valid
  @NonNull
  private final RbsReportDefinition productionProperties;

  ReportInstanceReferenceDto createReport(OffsetDateTime from, OffsetDateTime to) {
    ReportRange range = of(from, to);
    List<String> indexes = Collections.singletonList(productionProperties.getIndexName());
    String fileName = getFileName(range);
    return reportService.createReportInstance(range, fileName, indexes, productionProperties);
  }

  private static String getFileName(ReportRange range) {
    return format(FILE_NAME, range.getFromAsLocalDate(), range.getToAsLocalDate());
  }
}
