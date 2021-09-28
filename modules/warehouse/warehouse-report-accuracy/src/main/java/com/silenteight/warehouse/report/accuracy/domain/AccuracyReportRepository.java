package com.silenteight.warehouse.report.accuracy.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

interface AccuracyReportRepository extends Repository<AccuracyReport, Long> {

  AccuracyReport save(AccuracyReport report);

  AccuracyReport getById(long id);

  void deleteById(long id);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM warehouse_report_accuracy WHERE created_at < :offsetDateTime",
      nativeQuery = true)
  int removeAllByCreatedAtBefore(OffsetDateTime offsetDateTime);

  List<AccuracyReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
