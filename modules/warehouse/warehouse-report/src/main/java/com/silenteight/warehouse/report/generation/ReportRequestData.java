package com.silenteight.warehouse.report.generation;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class ReportRequestData {

  Long domainId;
  String fileStorageName;
  Optional<OffsetDateTime> from;
  Optional<OffsetDateTime> to;
  List<String> sqlTemplates;
  String selectSqlQuery;
  String analysisId;
}
