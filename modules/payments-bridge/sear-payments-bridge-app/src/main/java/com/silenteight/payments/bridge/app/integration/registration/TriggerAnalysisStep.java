package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.ae.alertregistration.domain.TriggerAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.TriggerAlertAnalysisUseCase;
import com.silenteight.payments.bridge.common.event.SolvingAlertRegisteredEvent;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
class TriggerAnalysisStep {

  private final TriggerAlertAnalysisUseCase triggerAlertAnalysisUseCase;
  private final AlertRetentionStep alertRetentionStep;
  private final IndexAlertRegisteredStep indexAlertRegisteredStep;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Timed(percentiles = {0.5, 0.95, 0.99}, histogram = true)
  void invoke(Context ctx) {
    log.info("Adding alert [{}], ae name: [{}] to analysis",
        ctx.getAlertId(), ctx.getAlertName());

    var request = TriggerAlertRequest.builder()
        .alertNames(List.of(ctx.getAlertName()))
        .build();
    triggerAlertAnalysisUseCase.triggerAlertAnalysis(request);
    alertRetentionStep.invoke(ctx);
    indexAlertRegisteredStep.invoke(ctx);
    applicationEventPublisher.publishEvent(new SolvingAlertRegisteredEvent(ctx.getAeAlert()));
  }

}
