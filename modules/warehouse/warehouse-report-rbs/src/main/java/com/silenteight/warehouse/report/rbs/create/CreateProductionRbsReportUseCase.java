package com.silenteight.warehouse.report.rbs.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.reporting.RbsReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;

@RequiredArgsConstructor
class CreateProductionRbsReportUseCase {

  @NonNull
  private final RbsReportService reportService;
  @Valid
  @NonNull
  private final RbsReportDefinition productionProperties;

  ReportInstanceReferenceDto createReport(OffsetDateTime from, OffsetDateTime to) {
    ReportRange range = of(from, to);
    List<String> indexes = Collections.singletonList(productionProperties.getIndexName());
    return reportService.createReportInstance(range, indexes, productionProperties);
  }
}
