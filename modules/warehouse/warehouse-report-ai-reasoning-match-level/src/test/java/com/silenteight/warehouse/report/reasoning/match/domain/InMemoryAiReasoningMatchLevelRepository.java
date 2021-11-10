package com.silenteight.warehouse.report.reasoning.match.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class InMemoryAiReasoningMatchLevelRepository
    extends BasicInMemoryRepository<AiReasoningMatchLevelReport>
    implements AiReasoningMatchLevelReportRepository {

  @Override
  public void deleteAll(Iterable<AiReasoningMatchLevelReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<AiReasoningMatchLevelReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
