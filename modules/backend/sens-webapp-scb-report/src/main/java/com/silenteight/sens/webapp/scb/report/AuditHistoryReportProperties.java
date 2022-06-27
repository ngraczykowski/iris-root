package com.silenteight.sens.webapp.scb.report;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@RequiredArgsConstructor
@ConstructorBinding
@Data
class AuditHistoryReportProperties {

  @NotBlank
  private String cron;
}
