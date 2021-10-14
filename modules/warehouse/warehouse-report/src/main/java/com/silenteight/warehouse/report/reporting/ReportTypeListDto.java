package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import javax.annotation.Nullable;

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
    @Nullable
    private String type;
  }
}
