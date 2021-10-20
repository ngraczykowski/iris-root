package com.silenteight.warehouse.report.billing.v1.domain;

import org.springframework.data.repository.Repository;

interface DeprecatedBillingReportRepository extends Repository<DeprecatedBillingReport, Long> {

  DeprecatedBillingReport save(DeprecatedBillingReport report);

  DeprecatedBillingReport getById(long id);

  void deleteById(long id);
}
