package com.silenteight.warehouse.report.billing.domain;

import org.springframework.data.repository.Repository;

interface BillingReportRepository extends Repository<BillingReport, Long> {

  BillingReport save(BillingReport report);

  BillingReport getById(long id);

  void deleteById(long id);
}
