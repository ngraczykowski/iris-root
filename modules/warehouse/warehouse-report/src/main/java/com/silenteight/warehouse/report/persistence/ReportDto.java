package com.silenteight.warehouse.report.persistence;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ReportDto {

  Long id;
  String name;
  String fileStorageName;
  ReportRange range;
  OffsetDateTime createdAt;
  ReportState state;
  ReportFileExtension extension;
}
