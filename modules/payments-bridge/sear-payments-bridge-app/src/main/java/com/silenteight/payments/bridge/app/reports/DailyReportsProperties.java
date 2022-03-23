package com.silenteight.payments.bridge.app.reports;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.daily-reports-remove")
class DailyReportsProperties {

  private String bucket;
  private String filePrefix;
  private Long reportExpirationInMonths;

}
