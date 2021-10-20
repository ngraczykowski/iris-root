package com.silenteight.warehouse.report.billing.v1.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

public class DeprecatedInMemoryBillingRepository
    extends BasicInMemoryRepository<DeprecatedBillingReport>
    implements DeprecatedBillingReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }
}
