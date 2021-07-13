package com.silenteight.warehouse.report.rbs.domain;

import org.springframework.data.repository.Repository;

interface RbsReportRepository extends Repository<Report, Long> {

  Report save(Report report);

  Report getById(long id);

  void deleteById(long id);
}
