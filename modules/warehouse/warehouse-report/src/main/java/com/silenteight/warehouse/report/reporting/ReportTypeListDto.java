package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class ReportTypeListDto {

  @NonNull
  List<ReportTypeDto> reportTypes;

  @Data
  @Builder
  public static class ReportTypeDto {

    @NonNull
    private String name;
    @NonNull
    private String type;
    @NonNull
    private String title;
  }
}
