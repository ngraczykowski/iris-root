package com.silenteight.warehouse.report.billing.download;

import com.silenteight.warehouse.report.billing.domain.dto.BillingReportDto;

public interface ReportDataQuery {

  BillingReportDto getReport(long id);
}
