package com.silenteight.warehouse.report.metrics.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface MetricsReportRepository extends Repository<MetricsReport, Long> {

  MetricsReport save(MetricsReport report);

  MetricsReport getById(long id);

  void deleteById(long id);

  void deleteAll(Iterable<MetricsReport> reports);

  List<MetricsReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
