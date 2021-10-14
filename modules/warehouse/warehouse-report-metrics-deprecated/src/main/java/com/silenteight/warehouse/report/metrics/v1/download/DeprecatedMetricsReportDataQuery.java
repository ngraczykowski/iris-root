package com.silenteight.warehouse.report.metrics.v1.download;

import com.silenteight.warehouse.report.metrics.v1.domain.dto.ReportDto;

public interface DeprecatedMetricsReportDataQuery {

  ReportDto getReport(long id);
}
