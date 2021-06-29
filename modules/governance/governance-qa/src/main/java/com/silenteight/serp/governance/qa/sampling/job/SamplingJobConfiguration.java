package com.silenteight.serp.governance.qa.sampling.job;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.serp.governance.qa.sampling.domain.AlertSamplingService;
import com.silenteight.serp.governance.qa.sampling.domain.SamplingDomainConfiguration;
import com.silenteight.serp.governance.qa.sampling.generator.AlertsGeneratorService;
import com.silenteight.serp.governance.qa.sampling.generator.SamplingGeneratorConfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.validation.Valid;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(QaScheduledSamplingProperties.class)
@Import({SamplingDomainConfiguration.class, SamplingGeneratorConfiguration.class })
class SamplingJobConfiguration {

  @Bean
  AuditJobScheduler auditJobScheduler(AlertsGenerator auditHandlerJob) {
    return new AuditJobScheduler(auditHandlerJob);
  }

  @Bean
  AlertsGenerator auditHandlerJob(
      AlertSamplingByState alertSamplingQuery,
      AlertSamplingService alertSamplingService,
      AlertsGeneratorService alertsGeneratorService,
      DateRangeProvider dateRangeProvider,
      CronExecutionTimeProvider cronExecutionTimeProvider) {

    return new AlertsGenerator(
        alertSamplingQuery,
        alertSamplingService,
        alertsGeneratorService,
        dateRangeProvider,
        cronExecutionTimeProvider,
        DefaultTimeSource.INSTANCE);
  }

  @Bean
  DateRangeProvider dateRangeProvider(@Valid QaScheduledSamplingProperties properties) {
    return new DateRangeProvider(properties.getAlertGeneratorCron(), DefaultTimeSource.INSTANCE);
  }

  @Bean
  CronExecutionTimeProvider scheduledExecutionTimeProvider(
      @Valid QaScheduledSamplingProperties properties) {

    return new CronExecutionTimeProvider(properties.getAuditCron(),
        DefaultTimeSource.INSTANCE);
  }
}
