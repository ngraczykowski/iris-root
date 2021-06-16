package com.silenteight.serp.governance.qa.check;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
class ViewingDecisionJobScheduler {

  @NonNull
  private final ViewingDecisionHandlerJob viewingDecisionHandlerJob;

  @Scheduled(
      fixedDelayString = "${serp.governance.qa.scheduled.viewing-decision.max-ms}",
      initialDelayString = "${serp.governance.qa.scheduled.viewing-decision.max-ms}")
  void handleDecisionState() {
    viewingDecisionHandlerJob.run();
  }
}