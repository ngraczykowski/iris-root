package com.silenteight.warehouse.report.billing.v1.download;

import com.silenteight.warehouse.report.billing.v1.domain.dto.ReportDto;

public interface DeprecatedReportDataQuery {

  ReportDto getReport(long id);
}
