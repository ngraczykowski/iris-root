package com.silenteight.warehouse.report.billing.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

public class InMemoryBillingRepository
    extends BasicInMemoryRepository<BillingReport>
    implements BillingReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }
}
