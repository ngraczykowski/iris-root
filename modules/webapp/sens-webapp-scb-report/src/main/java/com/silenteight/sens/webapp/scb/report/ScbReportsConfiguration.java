package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.api.UserQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
}
