package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;

public interface DeprecatedMetricsReportDataQuery {

  ReportDto getReport(long id);
}
