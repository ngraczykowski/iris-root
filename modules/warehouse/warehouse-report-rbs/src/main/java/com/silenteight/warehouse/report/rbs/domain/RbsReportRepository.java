package com.silenteight.warehouse.report.rbs.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;

interface RbsReportRepository extends Repository<RbsReport, Long> {

  RbsReport save(RbsReport report);

  RbsReport getById(long id);

  void deleteById(long id);

  long deleteByCreatedAtBefore(OffsetDateTime dayToRemoveReports);
}
