package com.silenteight.warehouse.report.accuracy.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class InMemoryAccuracyRepository
    extends BasicInMemoryRepository<AccuracyReport>
    implements AccuracyReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }

  @Override
  public int removeAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    // TODO: (WEB-1435)
    throw new IllegalStateException("Not implemented");
  }

  @Override
  public List<AccuracyReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    throw new IllegalStateException("Not implemented");
  }
}
