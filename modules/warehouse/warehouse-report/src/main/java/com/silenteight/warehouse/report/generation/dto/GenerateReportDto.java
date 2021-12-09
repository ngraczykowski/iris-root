package com.silenteight.warehouse.report.generation.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.sql.dto.SqlExecutorDto;

@Value
@Builder
public class GenerateReportDto {

  @NonNull
  String reportName;
  @NonNull
  SqlExecutorDto sqlExecutorDto;
}
