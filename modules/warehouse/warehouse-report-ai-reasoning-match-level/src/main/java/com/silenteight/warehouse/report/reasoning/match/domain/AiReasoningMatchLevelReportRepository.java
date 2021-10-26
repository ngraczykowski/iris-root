package com.silenteight.warehouse.report.reasoning.match.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface AiReasoningMatchLevelReportRepository
    extends Repository<AiReasoningMatchLevelReport, Long> {

  AiReasoningMatchLevelReport save(AiReasoningMatchLevelReport report);

  AiReasoningMatchLevelReport getById(long id);

  void deleteAll(Iterable<AiReasoningMatchLevelReport> reports);

  List<AiReasoningMatchLevelReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
