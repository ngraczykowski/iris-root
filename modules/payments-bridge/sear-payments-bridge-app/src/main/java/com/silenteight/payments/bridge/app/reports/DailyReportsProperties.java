package com.silenteight.payments.bridge.app.reports;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.daily-reports-remove")
class DailyReportsProperties {

  private static final Long DEFAULT_REPORT_EXPIRATION_IN_MONTHS = 6L;

  private String bucket;
  private String filePrefix;
  private Long reportExpirationInMonths = DEFAULT_REPORT_EXPIRATION_IN_MONTHS;

}
