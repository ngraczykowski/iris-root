package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.warehouse.report.metrics.domain.dto.ReportDto;

public interface MetricsReportDataQuery {

  ReportDto getReport(long id);
}
