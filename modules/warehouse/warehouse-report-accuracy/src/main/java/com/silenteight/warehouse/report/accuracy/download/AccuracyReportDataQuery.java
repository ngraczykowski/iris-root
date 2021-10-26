package com.silenteight.warehouse.report.accuracy.download;

import com.silenteight.warehouse.report.accuracy.domain.dto.AccuracyReportDto;

public interface AccuracyReportDataQuery {

  AccuracyReportDto getAccuracyReportDto(long id);
}
