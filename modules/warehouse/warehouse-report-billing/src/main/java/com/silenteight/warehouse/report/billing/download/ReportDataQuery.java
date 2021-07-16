package com.silenteight.warehouse.report.billing.download;

import com.silenteight.warehouse.report.billing.domain.dto.ReportDto;

public interface ReportDataQuery {

  ReportDto getReport(long id);
}
