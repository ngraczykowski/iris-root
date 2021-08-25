package com.silenteight.warehouse.report.rbs.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

interface RbsReportRepository extends Repository<RbsReport, Long> {

  RbsReport save(RbsReport report);

  RbsReport getById(long id);

  void deleteById(long id);

  @Transactional
  @Modifying
  @Query(value = "DELETE FROM warehouse_report_rbs WHERE created_at < :dayToRemoveReports",
      nativeQuery = true)
  int deleteByCreatedAtBefore(OffsetDateTime dayToRemoveReports);
}
