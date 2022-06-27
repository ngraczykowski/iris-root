package com.silenteight.sens.webapp.audit.report;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.audit.api.list.ListAuditLogsQuery;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@EnableConfigurationProperties(AuditReportProperties.class)
@AllArgsConstructor
class AuditReportConfiguration {

  @Bean
  AuditReportGenerator auditReportGenerator(@NonNull ListAuditLogsQuery listAuditLogsQuery,
      @Validated @NotNull AuditReportProperties properties) {

    return new AuditReportGenerator(DefaultTimeSource.INSTANCE, DigitsOnlyDateFormatter.INSTANCE,
        listAuditLogsQuery, properties);
  }
}
