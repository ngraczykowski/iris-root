package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class DeprecatedInMemoryMatchLevelAiReasoningRepository
    extends BasicInMemoryRepository<DeprecatedAiReasoningMatchLevelReport>
    implements DeprecatedAiReasoningMatchLevelReportRepository {

  @Override
  public void deleteAll(Iterable<DeprecatedAiReasoningMatchLevelReport> reports) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public List<DeprecatedAiReasoningMatchLevelReport> getAllByCreatedAtBefore(
      OffsetDateTime offsetDateTime) {

    throw new UnsupportedOperationException("Not implemented");
  }
}
