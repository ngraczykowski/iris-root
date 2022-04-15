package com.silenteight.sens.webapp.audit.report;

import lombok.NonNull;

import com.silenteight.sens.webapp.audit.api.list.ListAuditLogsQuery;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AuditReportConfiguration {

  @Bean
  AuditReportGenerator auditReportGenerator(@NonNull ListAuditLogsQuery listAuditLogsQuery) {
    return new AuditReportGenerator(
        DefaultTimeSource.INSTANCE, DigitsOnlyDateFormatter.INSTANCE, listAuditLogsQuery);
  }
}
