package com.silenteight.warehouse.report.reasoning.download;

import com.silenteight.warehouse.report.reasoning.domain.dto.AiReasoningReportDto;

public interface AiReasoningReportDataQuery {

  AiReasoningReportDto getAiReasoningReportDto(long id);
}
