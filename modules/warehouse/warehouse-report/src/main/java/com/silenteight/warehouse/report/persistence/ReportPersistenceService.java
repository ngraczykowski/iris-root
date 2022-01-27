package com.silenteight.warehouse.report.persistence;

import java.time.OffsetDateTime;
import java.util.List;

public interface ReportPersistenceService {
  ReportDto save(ReportRange range, String type, String id);

  void generationFail(Long id);

  void generationSuccessful(Long id);

  void generationStarted(Long id);

  List<ReportDto> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);

  void deleteAll(List<ReportDto> reports);

  ReportDto getReport(long id);
}
