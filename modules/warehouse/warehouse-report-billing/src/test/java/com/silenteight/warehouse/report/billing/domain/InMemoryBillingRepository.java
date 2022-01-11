package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class InMemoryBillingRepository
    extends BasicInMemoryRepository<BillingReport>
    implements BillingReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }

  @Override
  public void deleteAll(Iterable<BillingReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<BillingReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
