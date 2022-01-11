package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class InMemoryMetricsRepository
    extends BasicInMemoryRepository<MetricsReport>
    implements MetricsReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }

  @Override
  public void deleteAll(Iterable<MetricsReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<MetricsReport> getAllByCreatedAtBefore(
      OffsetDateTime offsetDateTime) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
