package com.silenteight.warehouse.report.metrics.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

interface MetricsReportRepository extends Repository<MetricsReport, Long> {

  MetricsReport save(MetricsReport report);

  MetricsReport getById(long id);

  void deleteById(long id);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM warehouse_report_metrics WHERE created_at < :offsetDateTime",
      nativeQuery = true)
  int removeAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
