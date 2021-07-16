package com.silenteight.warehouse.report.rbs.domain;

import org.springframework.data.repository.Repository;

interface RbsReportRepository extends Repository<RbsReport, Long> {

  RbsReport save(RbsReport report);

  RbsReport getById(long id);

  void deleteById(long id);
}
