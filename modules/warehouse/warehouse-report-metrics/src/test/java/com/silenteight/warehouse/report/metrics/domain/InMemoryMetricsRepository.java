package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;

public class InMemoryMetricsRepository
    extends BasicInMemoryRepository<MetricsReport>
    implements MetricsReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }

  @Override
  public int removeAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    // TODO: (WEB-1435)
    throw new UnsupportedOperationException("Not implemented");
  }
}
