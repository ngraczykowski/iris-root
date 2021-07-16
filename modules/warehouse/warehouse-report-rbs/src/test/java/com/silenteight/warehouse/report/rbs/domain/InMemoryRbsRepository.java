package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

public class InMemoryRbsRepository
    extends BasicInMemoryRepository<RbsReport>
    implements RbsReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }
}
