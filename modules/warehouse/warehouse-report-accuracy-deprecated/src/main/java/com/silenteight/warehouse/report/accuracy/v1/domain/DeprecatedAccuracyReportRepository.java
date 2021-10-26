package com.silenteight.warehouse.report.accuracy.v1.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface DeprecatedAccuracyReportRepository extends Repository<DeprecatedAccuracyReport, Long> {

  DeprecatedAccuracyReport save(DeprecatedAccuracyReport report);

  DeprecatedAccuracyReport getById(long id);

  void deleteAll(Iterable<DeprecatedAccuracyReport> reports);

  List<DeprecatedAccuracyReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
