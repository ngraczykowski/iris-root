package com.silenteight.simulator.management.timeout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseSimulationTimeoutValidator implements SimulationTimeoutValidator {

  void doLog(boolean result) {
    if (result)
      log.info(getTimeoutMessage());
    else
      log.debug(getNoTimeoutMessage());
  }

  protected abstract String getTimeoutMessage();

  protected abstract String getNoTimeoutMessage();
}
