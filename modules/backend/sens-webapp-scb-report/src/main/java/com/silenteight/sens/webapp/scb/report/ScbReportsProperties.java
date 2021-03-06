package com.silenteight.sens.webapp.scb.report;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "report-scb")
@RequiredArgsConstructor
@ConstructorBinding
@Data
class ScbReportsProperties {

  @NotBlank
  private final String dir;

  @NotNull
  @Valid
  private final IdManagementReportProperties idManagement;

  @NotNull
  @Valid
  private final AuditHistoryReportProperties auditHistory;

  @NotNull
  @Valid
  private final UserAuthActivityReportProperties userAuthActivity;

  public String idManagementCronExpression() {
    return idManagement.getCron();
  }

  public String auditHistoryCronExpression() {
    return auditHistory.getCron();
  }

  public String userAuthActivityCronExpression() {
    return userAuthActivity.getCron();
  }
}
