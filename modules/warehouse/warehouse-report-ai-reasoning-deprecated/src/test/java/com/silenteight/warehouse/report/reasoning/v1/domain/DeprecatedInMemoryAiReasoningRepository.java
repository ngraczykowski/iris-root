package com.silenteight.warehouse.report.reasoning.v1.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class DeprecatedInMemoryAiReasoningRepository
    extends BasicInMemoryRepository<DeprecatedAiReasoningReport>
    implements DeprecatedAiReasoningReportRepository {

  @Override
  public void deleteAll(Iterable<DeprecatedAiReasoningReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<DeprecatedAiReasoningReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
