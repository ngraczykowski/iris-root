package com.silenteight.warehouse.report.sm.domain;

import org.springframework.data.repository.Repository;

interface SimulationMetricsReportRepository extends Repository<SimulationMetricsReport, Long> {

  SimulationMetricsReport save(SimulationMetricsReport report);

  SimulationMetricsReport getById(long id);

  void deleteById(long id);
}
