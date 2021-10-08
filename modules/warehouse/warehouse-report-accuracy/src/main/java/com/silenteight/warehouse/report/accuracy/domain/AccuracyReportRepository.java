package com.silenteight.warehouse.report.accuracy.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface AccuracyReportRepository extends Repository<AccuracyReport, Long> {

  AccuracyReport save(AccuracyReport report);

  AccuracyReport getById(long id);

  void deleteAll(Iterable<AccuracyReport> reports);

  List<AccuracyReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
