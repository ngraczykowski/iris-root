package com.silenteight.warehouse.report.billing.domain;

import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface BillingReportRepository extends Repository<BillingReport, Long> {

  BillingReport save(BillingReport report);

  BillingReport getById(long id);

  void deleteById(long id);

  void deleteAll(Iterable<BillingReport> reports);

  List<BillingReport> getAllByCreatedAtBefore(OffsetDateTime offsetDateTime);
}
