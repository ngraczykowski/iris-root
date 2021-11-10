package com.silenteight.warehouse.report.reasoning.match.v1.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface DeprecatedAiReasoningMatchLevelReportRepository
    extends Repository<DeprecatedAiReasoningMatchLevelReport, Long> {

  DeprecatedAiReasoningMatchLevelReport save(DeprecatedAiReasoningMatchLevelReport report);

  DeprecatedAiReasoningMatchLevelReport getById(long id);

  void deleteAll(Iterable<DeprecatedAiReasoningMatchLevelReport> reports);

  List<DeprecatedAiReasoningMatchLevelReport> getAllByCreatedAtBefore(
      OffsetDateTime offsetDateTime);
}
