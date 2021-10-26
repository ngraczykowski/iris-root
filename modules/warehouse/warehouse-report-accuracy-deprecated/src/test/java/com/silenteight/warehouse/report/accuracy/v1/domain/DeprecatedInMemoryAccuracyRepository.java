package com.silenteight.warehouse.report.accuracy.v1.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class DeprecatedInMemoryAccuracyRepository
    extends BasicInMemoryRepository<DeprecatedAccuracyReport>
    implements DeprecatedAccuracyReportRepository {

  @Override
  public void deleteAll(Iterable<DeprecatedAccuracyReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<DeprecatedAccuracyReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
