package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class ReportsDefinitionListDto {

  @NonNull
  List<ReportDefinitionDto> reportDefinitionDtoList;

  @Data
  @Builder
  public static class ReportDefinitionDto {

    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String reportType;
  }
}
