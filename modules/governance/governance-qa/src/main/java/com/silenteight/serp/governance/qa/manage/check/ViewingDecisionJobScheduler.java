package com.silenteight.serp.governance.qa.manage.check;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
class ViewingDecisionJobScheduler {

  @NonNull
  private final ViewingDecisionHandlerJob viewingDecisionHandlerJob;

  @Scheduled(
      fixedDelayString = "${serp.governance.qa.viewing.max-state-reset-delay-ms}",
      initialDelayString = "${serp.governance.qa.viewing.max-state-reset-delay-ms}")
  void handleDecisionState() {
    viewingDecisionHandlerJob.run();
  }
}