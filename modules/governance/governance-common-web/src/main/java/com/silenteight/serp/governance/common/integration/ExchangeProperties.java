package com.silenteight.serp.governance.common.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.messaging.exchange")
class ExchangeProperties {

  @NotBlank
  String analytics;

  @NotBlank
  String model;

  @NotBlank
  String notification;

  @NotBlank
  String solutionDiscrepancy;

  @NotBlank
  String govQa;

  @NotBlank
  String govEvent;
}
