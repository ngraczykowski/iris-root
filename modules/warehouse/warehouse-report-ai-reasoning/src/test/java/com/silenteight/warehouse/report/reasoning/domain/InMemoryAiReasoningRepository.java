package com.silenteight.warehouse.report.reasoning.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class InMemoryAiReasoningRepository
    extends BasicInMemoryRepository<AiReasoningReport>
    implements AiReasoningReportRepository {

  @Override
  public void deleteAll(Iterable<AiReasoningReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<AiReasoningReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
