package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.sep.usermanagement.api.UserListQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ScbReportsConfiguration {

  @Bean
  AccountsReportGenerator accountsReportGenerator(UserListQuery userListQuery) {
    return new AccountsReportGenerator(
        userListQuery, DefaultTimeSource.INSTANCE, new ScbReportDateFormatter());
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
