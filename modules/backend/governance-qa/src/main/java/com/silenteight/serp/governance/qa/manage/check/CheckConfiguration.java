package com.silenteight.serp.governance.qa.manage.check;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.validation.Valid;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(QaScheduledViewingDecisionProperties.class)
class CheckConfiguration {

  @Bean
  ViewingDecisionHandlerJob decisionStatusHandlerJob(
      @Valid QaScheduledViewingDecisionProperties qaScheduledViewingDecisionProperties,
      DecisionService decisionService) {

    return new ViewingDecisionHandlerJob(
        qaScheduledViewingDecisionProperties.getMaxStateResetDelayMs(),
        decisionService, DefaultTimeSource.INSTANCE);
  }

  @Bean
  ViewingDecisionJobScheduler decisionStateJobScheduler(
      ViewingDecisionHandlerJob viewingDecisionHandlerJob) {

    return new ViewingDecisionJobScheduler(viewingDecisionHandlerJob);
  }
}
