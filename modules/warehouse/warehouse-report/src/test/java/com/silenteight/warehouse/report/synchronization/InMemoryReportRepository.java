package com.silenteight.warehouse.report.synchronization;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class InMemoryReportRepository extends BasicInMemoryRepository<ReportEntity>
    implements ReportRepository {

  @Override
  public Set<String> getKibanaReportInstanceIds() {
    return stream()
        .map(ReportEntity::getKibanaReportInstanceId)
        .collect(toSet());
  }

  @Override
  public Set<ReportEntity> findByTenant(String tenant) {
    return stream()
        .filter(r -> r.hasTenant(tenant))
        .collect(toSet());
  }
}
