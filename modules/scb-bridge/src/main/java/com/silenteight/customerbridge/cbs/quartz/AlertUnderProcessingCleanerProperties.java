package com.silenteight.customerbridge.cbs.quartz;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Data
@Component
@Validated
@ConfigurationProperties("serp.scb.bridge.alert.cleaner")
class AlertUnderProcessingCleanerProperties {

  private String cronExpression;
  private Duration offset;
}
