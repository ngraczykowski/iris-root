package com.silenteight.warehouse.report.rbs.download;

import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;

public interface ReportDataQuery {

  ReportDto getReport(long id);
}
