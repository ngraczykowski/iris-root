package com.silenteight.warehouse.report.synchronization;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Set;

interface ReportRepository extends Repository<ReportEntity, Long> {

  ReportEntity save(ReportEntity reportEntity);

  @Query("SELECT r.kibanaReportInstanceId from ReportEntity r")
  Set<String> getKibanaReportInstanceIds();

  Set<ReportEntity> findByTenant(String tenant);
}
