package com.silenteight.warehouse.report.sm.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;

interface SimulationMetricsReportRepository extends Repository<SimulationMetricsReport, Long> {

  SimulationMetricsReport save(SimulationMetricsReport report);

  SimulationMetricsReport getById(long id);

  void deleteById(long id);

  long removeAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
