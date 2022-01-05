package com.silenteight.warehouse.report.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@ConstructorBinding
@Getter
@Validated
@ConfigurationProperties(prefix = "warehouse.report")
public class ReportProperties {

  AiReasoningReportProperties aiReasoning;

  AccuracyReportProperties accuracy;

  AiReasoningMatchLevelReportProperties aiReasoningMatchLevel;

  BillingReportProperties billing;

  RbsReportProperties rbs;

  StatisticsProperties statistics;

  MetricsReportProperties metrics;

}
