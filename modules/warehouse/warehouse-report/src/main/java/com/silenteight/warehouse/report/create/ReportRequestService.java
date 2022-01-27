package com.silenteight.warehouse.report.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.generation.ReportGenerationService;
import com.silenteight.warehouse.report.generation.ReportRequestData;
import com.silenteight.warehouse.report.persistence.ReportDto;
import com.silenteight.warehouse.report.persistence.ReportPersistenceService;
import com.silenteight.warehouse.report.persistence.ReportRange;

import java.time.OffsetDateTime;
import java.util.Optional;

@RequiredArgsConstructor
class ReportRequestService {

  @NonNull
  private final ReportPersistenceService reportPersistenceService;
  @NonNull
  private final ReportPropertiesMatcher reportPropertiesMatcher;
  @NonNull
  private final ReportGenerationService reportProvider;

  public ReportInstanceReferenceDto request(
      OffsetDateTime from,
      OffsetDateTime to,
      String type,
      String name) {

    ReportProperties properties = reportPropertiesMatcher.getFor(name, type);
    ReportDto report = reportPersistenceService.save(ReportRange.of(from, to, type), type, name);

    reportProvider.generate(ReportRequestData.builder()
        .domainId(report.getId())
        .fileStorageName(report.getFileStorageName())
        .analysisId(type)
        .selectSqlQuery(properties.getSelectSqlQuery())
        .sqlTemplates(properties.getSqlTemplates())
        .from(Optional.ofNullable(from))
        .to(Optional.ofNullable(to))
        .build());

    return new ReportInstanceReferenceDto(report.getId());
  }
}
