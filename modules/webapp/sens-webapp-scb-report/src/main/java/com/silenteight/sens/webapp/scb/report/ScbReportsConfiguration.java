package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sens.webapp.common.time.DefaultTimeSource;
import com.silenteight.sens.webapp.user.UserListQuery;

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
