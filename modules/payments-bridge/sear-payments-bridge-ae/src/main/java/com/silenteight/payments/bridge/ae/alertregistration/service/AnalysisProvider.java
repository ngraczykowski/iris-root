package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
class AnalysisProvider {

  private final GetCurrentAnalysisUseCase getCurrentAnalysisUseCase;
  private final AtomicReference<String> currentAnalysisName;

  AnalysisProvider(final GetCurrentAnalysisUseCase getCurrentAnalysisUseCase) {
    this.getCurrentAnalysisUseCase = getCurrentAnalysisUseCase;
    this.currentAnalysisName = new AtomicReference<>();
  }

  private static void debug(final String message, final Object... parameters) {
    if (log.isDebugEnabled()) {
      log.debug(message, parameters);
    }
  }

  @Scheduled(initialDelay = 1, fixedRate = 60, timeUnit = TimeUnit.SECONDS)
  void refresh() {
    debug("Refresh current analysis start!");
    var currentAnalysis = getCurrentAnalysisUseCase.getOrCreateAnalysis();
    debug("Refreshing finished!. Now determined analysis is: {}", currentAnalysis);
    currentAnalysisName.set(currentAnalysis);
  }

  @Timed
  public String currentAnalysis() {
    var currentAnalysis = this.currentAnalysisName.get();
    if (StringUtils.isBlank(currentAnalysis)) {
      debug("Current analysis is empty!. Refresh process started! ");
      this.refresh();
      currentAnalysis = this.currentAnalysisName.get();
    }
    log.info("Determined current analysis: {} ", currentAnalysis);

    return currentAnalysis;
  }
}
