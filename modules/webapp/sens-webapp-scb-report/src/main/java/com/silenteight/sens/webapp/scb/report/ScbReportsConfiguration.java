package com.silenteight.sens.webapp.scb.report;

import com.silenteight.auditing.bs.AuditingFinder;
import com.silenteight.sens.webapp.common.support.csv.FileLineWriter;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.api.UserQuery;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(ScbReportsProperties.class)
class ScbReportsConfiguration {

  @Bean
  AccountsReportGenerator accountsReportGenerator(UserQuery userQuery) {
    return new AccountsReportGenerator(
        userQuery, DefaultTimeSource.INSTANCE, new ScbReportDateFormatter());
  }

  @Bean
  EntitlementReportGenerator entitlementReportGenerator() {
    return new EntitlementReportGenerator(
        DefaultTimeSource.INSTANCE, new ScbReportDateFormatter());
  }

  @Bean
  SecurityMatrixReportGenerator securityMatrixReportGenerator() {
    return new SecurityMatrixReportGenerator();
  }

  @Bean
  IdManagementEventProvider idManagementEventProvider(AuditingFinder auditingFinder) {
    return new IdManagementEventProvider(auditingFinder);
  }

  @Bean
  IdManagementReportGenerator idManagementReportGenerator(
      IdManagementEventProvider idManagementEventProvider,
      ScbReportsProperties scbReportsProperties) {
    return new IdManagementReportGenerator(
        new DateRangeProvider(
            scbReportsProperties.idManagementCronExpression(), DefaultTimeSource.INSTANCE),
        idManagementEventProvider, scbReportsProperties.getDir(),
        new FileLineWriter(),
        DefaultTimeSource.INSTANCE);
  }
}
