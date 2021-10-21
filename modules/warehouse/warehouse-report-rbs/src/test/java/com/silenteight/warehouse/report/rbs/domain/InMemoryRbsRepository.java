package com.silenteight.warehouse.report.rbs.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;

class InMemoryRbsRepository
    extends BasicInMemoryRepository<RbsReport>
    implements RbsReportRepository {

  @Override
  public void deleteById(long id) {
    delete(getById(id));
  }

  @Override
  public int deleteByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    // TODO: It doesn't make sense to test test implementation, rewrite as integration test
    // (WEB-1435)
    throw new IllegalStateException("Not implemented");
  }
}
