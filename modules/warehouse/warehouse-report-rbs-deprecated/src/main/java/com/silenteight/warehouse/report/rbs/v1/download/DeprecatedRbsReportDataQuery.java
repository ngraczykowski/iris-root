package com.silenteight.warehouse.report.rbs.v1.download;

import com.silenteight.warehouse.report.rbs.v1.domain.dto.ReportDto;

public interface DeprecatedRbsReportDataQuery {

  ReportDto getReport(long id);
}
