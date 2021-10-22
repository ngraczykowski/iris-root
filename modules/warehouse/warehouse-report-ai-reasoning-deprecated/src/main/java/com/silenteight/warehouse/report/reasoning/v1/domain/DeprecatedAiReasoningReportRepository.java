package com.silenteight.warehouse.report.reasoning.v1.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface DeprecatedAiReasoningReportRepository
    extends Repository<DeprecatedAiReasoningReport, Long> {

  DeprecatedAiReasoningReport save(DeprecatedAiReasoningReport report);

  DeprecatedAiReasoningReport getById(long id);

  void deleteAll(Iterable<DeprecatedAiReasoningReport> reports);

  List<DeprecatedAiReasoningReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
