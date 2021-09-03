package com.silenteight.serp.governance.qa.retention.erase;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RetentionEraseConfiguration {

  @Bean
  EraseDecisionCommentUseCase eraseDecisionCommentUseCase(DecisionService decisionService) {
    return new EraseDecisionCommentUseCase(decisionService);
  }
}
