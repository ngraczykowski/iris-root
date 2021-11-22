package com.silenteight.warehouse.report.rbs.download;

import com.silenteight.warehouse.report.rbs.domain.dto.RbsReportDto;

public interface RbsReportDataQuery {

  RbsReportDto getRbsReport(long id);
}
