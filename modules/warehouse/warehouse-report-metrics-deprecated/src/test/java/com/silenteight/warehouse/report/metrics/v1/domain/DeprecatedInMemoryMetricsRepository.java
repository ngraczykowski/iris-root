package com.silenteight.warehouse.report.metrics.v1.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;

public class DeprecatedInMemoryMetricsRepository
    extends BasicInMemoryRepository<DeprecatedMetricsReport>
    implements DeprecatedMetricsReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }

  @Override
  public int removeAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    // TODO: (WEB-1435)
    throw new IllegalStateException("Not implemented");
  }
}
