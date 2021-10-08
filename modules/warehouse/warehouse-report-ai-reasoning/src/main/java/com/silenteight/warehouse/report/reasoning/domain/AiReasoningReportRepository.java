package com.silenteight.warehouse.report.reasoning.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface AiReasoningReportRepository extends Repository<AiReasoningReport, Long> {

  AiReasoningReport save(AiReasoningReport report);

  AiReasoningReport getById(long id);

  void deleteAll(Iterable<AiReasoningReport> reports);

  List<AiReasoningReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
