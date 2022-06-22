package com.silenteight.warehouse.report.availablereports;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
class ReportTypeListDto {

  @NonNull
  List<ReportTypeDto> reportTypes;

  @Value
  @Builder
  public static class ReportTypeDto {

    @NonNull
    String name;
    @NonNull
    String type;
    @NonNull
    String title;
    @NonNull
    @Default
    FilterDto filter = new FilterDto(FilterType.DATE_RANGE);
    @NonNull
    @Default
    DownloadType download = DownloadType.ASYNC;
  }
}
