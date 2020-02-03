package com.silenteight.sens.webapp.backend.reportscb;

import com.silenteight.sens.webapp.common.time.DefaultTimeSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ScbReportsConfiguration {

  @Bean
  EntitlementReportGenerator entitlementReportGenerator() {
    return new EntitlementReportGenerator(
        DefaultTimeSource.INSTANCE, new ScbReportDateFormatter());
  }

  @Bean
  AccountsReportGenerator accountsReportGenerator(UserListRepository repository) {
    return new AccountsReportGenerator(
        repository, DefaultTimeSource.INSTANCE, new ScbReportDateFormatter());
  }

  @Bean
  SecurityMatrixReportGenerator securityMatrixReportGenerator() {
    return new SecurityMatrixReportGenerator();
  }
}
