package com.silenteight.warehouse.report.accuracy.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class InMemoryAccuracyRepository
    extends BasicInMemoryRepository<AccuracyReport>
    implements AccuracyReportRepository {


  @Override
  public void deleteAll(Iterable<AccuracyReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<AccuracyReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
