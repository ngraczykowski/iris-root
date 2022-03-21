package com.silenteight.payments.bridge.app.integration.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.datasource.port.EtlUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class UniversalDataSourceStep {

  private final EtlUseCase etlUseCase;
  private final TriggerAnalysisStep triggerAnalysisStep;

  @Timed(percentiles = { 0.95, 0.99 }, histogram = true)
  void invoke(Context ctx) {
    etlUseCase.process(ctx.getAeAlert());
    triggerAnalysisStep.invoke(ctx);
  }

}
