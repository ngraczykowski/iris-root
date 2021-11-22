package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.warehouse.report.metrics.domain.dto.MetricsReportDto;

public interface MetricsReportDataQuery {

  MetricsReportDto getReport(long id);
}
