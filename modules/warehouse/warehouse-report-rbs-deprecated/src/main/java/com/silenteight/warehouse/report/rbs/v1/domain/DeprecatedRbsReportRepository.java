package com.silenteight.warehouse.report.rbs.v1.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

interface DeprecatedRbsReportRepository extends Repository<DeprecatedRbsReport, Long> {

  DeprecatedRbsReport save(DeprecatedRbsReport report);

  DeprecatedRbsReport getById(long id);

  void deleteById(long id);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM warehouse_report_rbs WHERE created_at < :dayToRemoveReports",
      nativeQuery = true)
  int deleteByCreatedAtBefore(OffsetDateTime dayToRemoveReports);
}
